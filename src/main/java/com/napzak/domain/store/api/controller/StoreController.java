package com.napzak.domain.store.api.controller;

import java.util.List;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.domain.store.api.StoreGenreFacade;
import com.napzak.domain.store.api.dto.GenrePreferenceResponse;
import com.napzak.domain.store.api.dto.LoginSuccessResponse;
import com.napzak.domain.store.api.dto.MyPageResponse;
import com.napzak.domain.store.api.dto.StoreInfoResponse;
import com.napzak.domain.store.api.dto.StoreLoginResponse;
import com.napzak.domain.store.api.exception.StoreSuccessCode;
import com.napzak.domain.store.api.service.LoginService;
import com.napzak.domain.store.api.service.StoreService;
import com.napzak.domain.store.core.vo.GenrePreference;
import com.napzak.domain.store.core.vo.Store;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.auth.client.dto.StoreSocialLoginRequest;
import com.napzak.global.auth.jwt.service.TokenService;
import com.napzak.global.common.exception.dto.SuccessResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/stores")
@RequiredArgsConstructor
public class StoreController implements StoreApi {

	private static final String REFRESH_TOKEN = "refreshToken";
	private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60;
	private final LoginService loginService;
	private final TokenService tokenService;
	private final StoreService storeService;
	private final StoreGenreFacade storeGenreFacade;

	@PostMapping("/login")
	@Override
	public ResponseEntity<SuccessResponse<StoreLoginResponse>> login(
		String authorizationCode,
		StoreSocialLoginRequest storeSocialLoginRequest,
		HttpServletResponse httpServletResponse
	) {
		LoginSuccessResponse successResponse = loginService.login(authorizationCode, storeSocialLoginRequest);
		ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, successResponse.refreshToken())
			.maxAge(COOKIE_MAX_AGE)
			.path("/")
			.secure(true)
			.sameSite("None")
			.httpOnly(true)
			.build();
		httpServletResponse.setHeader("Set-Cookie", cookie.toString());

		StoreLoginResponse response = StoreLoginResponse.of(
			successResponse.accessToken(),
			successResponse.nickname(),
			successResponse.role());

		return ResponseEntity.ok()
			.body(SuccessResponse.of(StoreSuccessCode.LOGIN_SUCCESS, response));
	}

	@PostMapping("/logout")
	@Override
	public ResponseEntity<SuccessResponse<Void>> logOut(
		@CurrentMember final Long storeId
	) {
		tokenService.deleteRefreshToken(storeId);
		return ResponseEntity.ok()
			.body(SuccessResponse.of(StoreSuccessCode.LOGOUT_SUCCESS, null));
	}

	@GetMapping("/mypage")
	@Override
	public ResponseEntity<SuccessResponse<MyPageResponse>> getMyPageInfo(
		@CurrentMember final Long storeId
	) {
		MyPageResponse myPageResponse = storeService.getMyPageInfo(storeId);
		return ResponseEntity.ok()
			.body(SuccessResponse.of(StoreSuccessCode.GET_MYPAGE_INFO_SUCCESS, myPageResponse));
	}

	@GetMapping("/{storeId}")
	@Override
	public ResponseEntity<SuccessResponse<StoreInfoResponse>> getStoreInfo(
		@PathVariable("storeId") Long storeId
	) {

		List<GenrePreference> genreList = storeService.getGenrePreferenceList(storeId);
		Store store = storeService.getStore(storeId);

		List<GenrePreferenceResponse> genrePreferenceResponses = genreList.stream()
			.map(genrePreference -> GenrePreferenceResponse.of(
				genrePreference.getGenreId(),
				storeGenreFacade.getGenreName(genrePreference.getGenreId())
			))
			.toList();

		StoreInfoResponse storeInfoResponse = StoreInfoResponse.of(
			store.getId(),
			store.getNickname(),
			store.getDescription(),
			store.getPhoto(),
			genrePreferenceResponses
		);

		return ResponseEntity.ok()
			.body(SuccessResponse.of(StoreSuccessCode.GET_STORE_INFO_SUCCESS, storeInfoResponse));
	}
}
