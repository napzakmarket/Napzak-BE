package com.napzak.domain.store.api.service;

import com.napzak.global.auth.client.service.GoogleSocialService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.api.dto.response.LoginSuccessResponse;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.Store;
import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;
import com.napzak.global.auth.client.dto.StoreSocialLoginRequest;
import com.napzak.global.auth.client.service.SocialService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

	private final SocialService kakaoSocialService;
	private final StoreRegistrationService storeRegistrationService;
	private final AuthenticationService authenticationService;
	private final StoreService storeService;
	private final GoogleSocialService googleSocialService;

	@Transactional
	public LoginSuccessResponse login(
		String authorizationCode,
		StoreSocialLoginRequest request) {
		try {
			// 소셜 서비스로부터 사용자 정보 조회
			StoreSocialInfoResponse storeInfo = findStoreInfo(authorizationCode, request);

			// 회원 정보를 DB에서 확인
			Long storeId = findOrRegisterStore(storeInfo);

			// 토큰 발급, 로그인 응답
			return returnLoginSuccessResponse(storeId, storeInfo);
		} catch (Exception e) {
			log.error("Login failed: ", e);
			throw e;
		}
	}

	//소셜 타입에 따라 사용자 정보 조회
	private StoreSocialInfoResponse findStoreInfo(
		String authorizationCode,
		StoreSocialLoginRequest request) {

		SocialService socialService = getSocialService(request.socialType());
		return socialService.login(authorizationCode, request);
	}

	//해당하는 소셜 서비스 반환
	private SocialService getSocialService(SocialType socialType) {
		return switch (socialType) {
			case KAKAO -> kakaoSocialService;
			case GOOGLE -> googleSocialService;
			default -> throw new RuntimeException();
		};
	}

	//기존 회원을 찾거나, 없으면 새로 멤버 등록
	private Long findOrRegisterStore(final StoreSocialInfoResponse storeSocialInfoResponse) {

		final String socialId = storeSocialInfoResponse.socialId();
		final SocialType socialType = storeSocialInfoResponse.socialType();

		boolean storeExits = storeService.checkStoreExistsBySocialIdAndSocialType(socialId, socialType);

		if (storeExits) {
			Store store = storeService.findStoreBySocialIdAndSocialType(socialId, socialType);
			return store.getId();
		}

		return storeRegistrationService.registerStoreWithStoreInfo(storeSocialInfoResponse);
	}

	private LoginSuccessResponse returnLoginSuccessResponse(
		Long storeId,
		StoreSocialInfoResponse storeSocialInfoResponse) {

		return authenticationService.generateLoginSuccessResponse(storeId, storeSocialInfoResponse);

	}

}
