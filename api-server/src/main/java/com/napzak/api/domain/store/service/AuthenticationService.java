package com.napzak.api.domain.store.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.api.domain.store.dto.response.AccessTokenGenerateResponse;
import com.napzak.api.domain.store.dto.response.LoginSuccessResponse;
import com.napzak.domain.store.crud.store.StoreRetriever;
import com.napzak.common.auth.client.dto.StoreSocialInfoResponse;
import com.napzak.common.auth.jwt.exception.TokenErrorCode;
import com.napzak.common.auth.jwt.provider.JwtTokenProvider;
import com.napzak.common.auth.jwt.provider.JwtValidationType;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.domain.store.vo.Store;
import com.napzak.common.auth.security.AdminAuthentication;
import com.napzak.common.auth.security.MemberAuthentication;
import com.napzak.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private static final String BEARER_PREFIX = "Bearer ";
	private final JwtTokenProvider jwtTokenProvider;
	private final TokenService tokenService;
	private final StoreRetriever storeRetriever;

	/**
	 * 사용자의 로그인 성공 시 Access Token과 Refresh Token을 생성하고,
	 * 로그인 성공 응답 객체(LoginSuccessResponse)를 반환하는 메서드.
	 *
	 * @param storeId 회원의 고유 ID
	 * @param storeSocialInfoResponse 로그인 시 외부로부터 전달된 회원 정보
	 * @return 로그인 성공 응답(LoginSuccessResponse)
	 */
	public LoginSuccessResponse generateLoginSuccessResponse(final Long storeId,
		final StoreSocialInfoResponse storeSocialInfoResponse) {

		Store store = storeRetriever.findStoreByStoreId(storeId);
		final Role role = store.getRole();
		final String nickname = store.getNickname();

		Collection<GrantedAuthority> authorities = List.of(role.toGrantedAuthority());

		log.info("Starting login success response generation for storeId: {}, nickname: {}, role: {}", storeId,
			nickname,
			role.getRoleName());

		UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationToken(storeId, role,
			authorities);
		String refreshToken = issueAndSaveRefreshToken(storeId, authenticationToken);
		String accessToken = jwtTokenProvider.issueAccessToken(authenticationToken);

		log.info("Login success for authorities: {}, accessToken: {}, refreshToken: {}", authorities, accessToken,
			refreshToken);

		return LoginSuccessResponse.of(accessToken, refreshToken, nickname, role);
	}

	/**
	 * 쿠키에서 "refreshToken" 값을 가져와 유효성을 검증하고,
	 * 유효한 Refresh Token일 경우 새로운 Access Token을 생성합니다.
	 *
	 * Refresh Token에서 사용자 ID와 Role 정보를 추출한 후,
	 * Role에 따라 Admin 또는 store 권한으로 새로운 Access Token을 발급합니다.
	 *
	 * @param refreshToken "사용자의 Refresh Token"
	 * @return 새로운 Access Token 정보가 포함된 AccessTokenGenerateResponse 객체
	 */
	@Transactional
	public AccessTokenGenerateResponse generateAccessTokenFromRefreshToken(final String refreshToken) {
		validateRefreshToken(refreshToken);

		Long storeId = jwtTokenProvider.getStoreIdFromJwt(refreshToken);
		verifyStoreIdWithStoredToken(refreshToken, storeId);

		Role role = jwtTokenProvider.getRoleFromJwt(refreshToken);
		Collection<GrantedAuthority> authorities = List.of(role.toGrantedAuthority());

		UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationToken(storeId, role, authorities);
		log.info("Generated new access token for storeId: {}, role: {}, authorities: {}",
			storeId, role.getRoleName(), authorities);

		return AccessTokenGenerateResponse.from(jwtTokenProvider.issueAccessToken(authenticationToken));
	}

	/**
	 * Refresh Token을 발급하고 저장하는 메서드.
	 * 발급된 Refresh Token을 TokenService에 저장
	 *
	 * @param storeId 회원의 고유 ID
	 * @param authenticationToken 사용자 인증 정보
	 * @return 발급된 Refresh Token
	 */
	private String issueAndSaveRefreshToken(Long storeId, UsernamePasswordAuthenticationToken authenticationToken) {
		String refreshToken = jwtTokenProvider.issueRefreshToken(authenticationToken);
		log.info("Issued new refresh token for storeId: {}", storeId);
		tokenService.saveRefreshToken(storeId, refreshToken);
		return refreshToken;
	}

	/**
	 * 사용자 Role에 따라 적절한 Authentication 객체(Admin 또는 Store)를 생성하는 메서드.
	 *
	 * @param storeId 회원의 고유 ID
	 * @param role 사용자 Role (ADMIN 또는 Store)
	 * @param authorities 사용자에게 부여된 권한 목록
	 * @return 생성된 Admin 또는 Store Authentication 객체
	 */
	private UsernamePasswordAuthenticationToken createAuthenticationToken(Long storeId, Role role,
		Collection<GrantedAuthority> authorities) {
		if (role == Role.ADMIN) {
			log.info("Creating AdminAuthentication for storeId: {}", storeId);
			return new AdminAuthentication(storeId, null, authorities);
		} else {
			log.info("Creating MemberAuthentication for storeId: {}", storeId);
			return new MemberAuthentication(storeId, null, authorities);
		}
	}

	private void validateRefreshToken(String refreshToken) {
		JwtValidationType validationType = jwtTokenProvider.validateToken(refreshToken);

		if (!validationType.equals(JwtValidationType.VALID_JWT)) {
			throw switch (validationType) {
				case EXPIRED_JWT_TOKEN -> new NapzakException(TokenErrorCode.REFRESH_TOKEN_EXPIRED_ERROR);
				case INVALID_JWT_TOKEN -> new NapzakException(TokenErrorCode.INVALID_REFRESH_TOKEN_ERROR);
				case INVALID_JWT_SIGNATURE -> new NapzakException(TokenErrorCode.REFRESH_TOKEN_SIGNATURE_ERROR);
				case UNSUPPORTED_JWT_TOKEN -> new NapzakException(TokenErrorCode.UNSUPPORTED_REFRESH_TOKEN_ERROR);
				case EMPTY_JWT -> new NapzakException(TokenErrorCode.REFRESH_TOKEN_EMPTY_ERROR);
				default -> new NapzakException(TokenErrorCode.UNKNOWN_REFRESH_TOKEN_ERROR);
			};
		}
	}

	private void verifyStoreIdWithStoredToken(String refreshToken, Long storeId) {
		Long storedStoreId = tokenService.findIdByRefreshToken(refreshToken);

		if (!storeId.equals(storedStoreId)) {
			log.error("StoreId mismatch: token does not match the stored refresh token");
			throw new NapzakException(TokenErrorCode.REFRESH_TOKEN_STORE_ID_MISMATCH_ERROR);
		}
	}

}
