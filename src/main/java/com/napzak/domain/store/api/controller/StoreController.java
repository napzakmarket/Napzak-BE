package com.napzak.domain.store.api.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.domain.store.api.StoreGenreFacade;
import com.napzak.domain.store.api.dto.GenrePreferenceResponse;
import com.napzak.domain.store.api.dto.LoginSuccessResponse;
import com.napzak.domain.store.api.dto.StoreLoginResponse;
import com.napzak.domain.store.api.dto.StoreNormalRegisterRequest;
import com.napzak.domain.store.api.dto.StoreNormalRegisterResponse;
import com.napzak.domain.store.api.exception.StoreErrorCode;
import com.napzak.domain.store.api.exception.StoreSuccessCode;
import com.napzak.domain.store.api.service.LoginService;
import com.napzak.domain.store.api.service.StoreRegistrationService;
import com.napzak.domain.store.api.service.StoreService;
import com.napzak.domain.store.core.vo.GenrePreference;
import com.napzak.domain.store.core.vo.Store;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.auth.client.dto.StoreSocialLoginRequest;
import com.napzak.global.auth.jwt.service.TokenService;
import com.napzak.global.common.exception.NapzakException;
import com.napzak.global.common.exception.dto.SuccessResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

	private static final String REFRESH_TOKEN = "refreshToken";
	private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60;
	private final LoginService loginService;
	private final TokenService tokenService;
	private final StoreRegistrationService storeRegistrationService;

	private final StoreGenreFacade storeGenreFacade;
	private final StoreService storeService;

	@PostMapping("/login")
	public ResponseEntity<SuccessResponse<StoreLoginResponse>> login(
		@RequestParam final
		String authorizationCode,
		@Valid @RequestBody final StoreSocialLoginRequest storeSocialLoginRequest,
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
	public ResponseEntity<SuccessResponse<Void>> logOut(
		@CurrentMember final Long storeId
	) {
		tokenService.deleteRefreshToken(storeId);
		return ResponseEntity.ok()
			.body(SuccessResponse.of(StoreSuccessCode.LOGOUT_SUCCESS, null));
	}

	@PostMapping("/register")
	public ResponseEntity<SuccessResponse<StoreNormalRegisterResponse>> register(
		@CurrentMember final Long currentMemberId,
		@Valid @RequestBody final StoreNormalRegisterRequest registerRequest
	) {
		//최대 장르 수 제한을 초과하면 예외
		int maximum_genre_count = 4;
		if (registerRequest.genrePreferenceList().size() > maximum_genre_count) {
			throw new NapzakException(StoreErrorCode.INVALID_GENRE_PREFERENCE_COUNT);
		}

		//입력한 선호 장르 내 중복이 있으면 예외
		Set<Long> uniqueGenres = new HashSet<>(registerRequest.genrePreferenceList());
		if (uniqueGenres.size() != registerRequest.genrePreferenceList().size()) {
			throw new NapzakException(StoreErrorCode.DUPLICATE_GENRE_PREFERENCES);
		}

		Store store = storeRegistrationService.registerStoreWithNormalInfo(currentMemberId, registerRequest);

		// #74에서 미리 구현해둔 코드. 머지 이후 메서드로 리팩토링 예정입니다.
		List<GenrePreference> genreList = storeService.getGenrePreferenceList(currentMemberId);
		List<Long> genreIds = genreList.stream().map(GenrePreference::getGenreId).toList();

		Map<Long, String> genreNamesMap = storeGenreFacade.getGenreNames(genreIds);

		List<GenrePreferenceResponse> genrePreferenceResponses = genreList.stream()
			.map(genrePreference -> GenrePreferenceResponse.of(genrePreference.getGenreId(),
				genreNamesMap.getOrDefault(genrePreference.getGenreId(), "기타")))
			.toList(); //

		StoreNormalRegisterResponse response = StoreNormalRegisterResponse.from(currentMemberId, store.getNickname(),
			genrePreferenceResponses);
		return ResponseEntity.ok()
			.body(SuccessResponse.of(StoreSuccessCode.NORMAL_INFO_REGISTER_SUCCESS, response));
	}
}
