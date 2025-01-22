package com.napzak.domain.store.api.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.domain.genre.api.dto.response.GenreNameDto;
import com.napzak.domain.genre.api.dto.response.GenreNameListResponse;
import com.napzak.domain.genre.api.exception.GenreErrorCode;
import com.napzak.domain.store.api.StoreGenreFacade;
import com.napzak.domain.store.api.dto.GenrePreferenceDto;
import com.napzak.domain.store.api.dto.GenrePreferenceRequest;
import com.napzak.domain.store.api.dto.LoginSuccessResponse;
import com.napzak.domain.store.api.dto.MyPageResponse;
import com.napzak.domain.store.api.dto.StoreInfoResponse;
import com.napzak.domain.store.api.dto.StoreLoginResponse;
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
	private final StoreRegistrationService storeRegistrationService;

	@PostMapping("/login")
	public ResponseEntity<SuccessResponse<StoreLoginResponse>> login(
		@RequestParam("authorizationCode") String authorizationCode,
		@RequestBody StoreSocialLoginRequest storeSocialLoginRequest,
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

		StoreLoginResponse response = StoreLoginResponse.of(successResponse.accessToken(), successResponse.nickname(),
			successResponse.role());

		return ResponseEntity.ok().body(SuccessResponse.of(StoreSuccessCode.LOGIN_SUCCESS, response));
	}

	@PostMapping("/logout")
	public ResponseEntity<SuccessResponse<Void>> logOut(
		@CurrentMember final Long currentStoreId
	) {
		tokenService.deleteRefreshToken(currentStoreId);
		return ResponseEntity.ok().body(SuccessResponse.of(StoreSuccessCode.LOGOUT_SUCCESS, null));
	}

	@GetMapping("/mypage")
	public ResponseEntity<SuccessResponse<MyPageResponse>> getMyPageInfo(
		@CurrentMember final Long currentStoreId
	) {
		Store store = storeService.getStore(currentStoreId);
		MyPageResponse myPageResponse = MyPageResponse.of(store.getId(), store.getNickname(), store.getPhoto());
		return ResponseEntity.ok().body(SuccessResponse.of(StoreSuccessCode.GET_MYPAGE_INFO_SUCCESS, myPageResponse));
	}

	@GetMapping("/{storeId}")
	public ResponseEntity<SuccessResponse<StoreInfoResponse>> getStoreInfo(
		@PathVariable("storeId") Long OnwerId,
		@CurrentMember final Long currentStoreId
	) {

		List<GenrePreference> genreList = storeService.getGenrePreferenceList(OnwerId);
		Store store = storeService.getStore(OnwerId);

		List<Long> genreIds = genreList.stream().map(GenrePreference::getGenreId).toList();

		Map<Long, String> genreNamesMap = storeGenreFacade.getGenreNames(genreIds);

		List<GenrePreferenceDto> genrePreferenceDto = genreList.stream()
			.map(genrePreference -> GenrePreferenceDto.of(genrePreference.getGenreId(),
				genreNamesMap.getOrDefault(genrePreference.getGenreId(), "기타")))
			.toList();

		StoreInfoResponse storeInfoResponse = StoreInfoResponse.of(store.getId(), store.getNickname(),
			store.getDescription(), store.getPhoto(), store.getCover(), genrePreferenceDto);

		return ResponseEntity.ok().body(SuccessResponse.of(StoreSuccessCode.GET_STORE_INFO_SUCCESS, storeInfoResponse));
	}

	@PostMapping("/register")
	public ResponseEntity<SuccessResponse<GenreNameListResponse>> register(
		@CurrentMember final Long currentMemberId,
		@Valid @RequestBody final GenrePreferenceRequest genrePreferenceList
	) {

		//최대 장르 수 제한을 초과하면 예외
		int maximum_genre_count = 4;
		if (genrePreferenceList.genreIds().size() > maximum_genre_count) {
			throw new NapzakException(StoreErrorCode.INVALID_GENRE_PREFERENCE_COUNT);
		}

		//입력한 선호 장르 내 중복이 있으면 예외
		Set<Long> uniqueGenres = new HashSet<>(genrePreferenceList.genreIds());
		if (uniqueGenres.size() != genrePreferenceList.genreIds().size()) {
			throw new NapzakException(StoreErrorCode.DUPLICATE_GENRE_PREFERENCES);
		}

		genrePreferenceList.genreIds().forEach(genreId -> {
			if (!storeGenreFacade.existsGenre(genreId)) {
				throw new NapzakException(GenreErrorCode.GENRE_NOT_FOUND);
			}
		});

		storeRegistrationService.registerGenrePreference(currentMemberId,
			genrePreferenceList.genreIds());

		List<GenrePreference> genreList = storeService.getGenrePreferenceList(currentMemberId);
		List<Long> genreIds = genreList.stream().map(GenrePreference::getGenreId).toList();

		Map<Long, String> genreNamesMap = storeGenreFacade.getGenreNames(genreIds);

		List<GenreNameDto> genrePreferenceResponse = genreList.stream()
			.map(genrePreference -> GenreNameDto.from(genrePreference.getGenreId(),
				genreNamesMap.getOrDefault(genrePreference.getGenreId(), "기타")))
			.toList();

		GenreNameListResponse response = GenreNameListResponse.fromWithoutCursor(genrePreferenceResponse);
		return ResponseEntity.ok()
			.body(SuccessResponse.of(StoreSuccessCode.GENRE_PREPERENCE_REGISTER_SUCCESS, response));
	}

}
