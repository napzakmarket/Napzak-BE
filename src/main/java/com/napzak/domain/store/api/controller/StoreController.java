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
import com.napzak.domain.genre.core.vo.Genre;
import com.napzak.domain.store.api.StoreGenreFacade;
import com.napzak.domain.store.api.dto.request.GenrePreferenceRequest;
import com.napzak.domain.store.api.dto.response.LoginSuccessResponse;
import com.napzak.domain.store.api.dto.response.MyPageResponse;
import com.napzak.domain.store.api.dto.response.StoreInfoResponse;
import com.napzak.domain.store.api.dto.response.StoreLoginResponse;
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

		List<GenreNameDto> genrePreferenceDto = genrePreferenceResponseGenerator(genreList);

		StoreInfoResponse storeInfoResponse = StoreInfoResponse.of(store.getId(), store.getNickname(),
			store.getDescription(), store.getPhoto(), store.getCover(), genrePreferenceDto);

		return ResponseEntity.ok().body(SuccessResponse.of(StoreSuccessCode.GET_STORE_INFO_SUCCESS, storeInfoResponse));
	}

	@PostMapping("genres/register")
	public ResponseEntity<SuccessResponse<GenreNameListResponse>> register(
		@CurrentMember final Long currentMemberId,
		@Valid @RequestBody final GenrePreferenceRequest genrePreferenceList
	) {

		List<Long> genreIds = genrePreferenceList.genreIds();

		int maximum_genre_count = 7;
		if (genrePreferenceList.genreIds().size() > maximum_genre_count) {
			throw new NapzakException(StoreErrorCode.INVALID_GENRE_PREFERENCE_COUNT);
		} //선호장르를 4개이상 등록하려고 했을 때 예외 발생

		Set<Long> uniqueGenres = new HashSet<>(genreIds);
		if (uniqueGenres.size() != genreIds.size()) {
			throw new NapzakException(StoreErrorCode.DUPLICATE_GENRE_PREFERENCES);
		} //입력한 선호장르 리스트에 중복이 있으면 예외 발생

		List<Genre> genreList = storeGenreFacade.findExistingGenreList(genreIds);
		List<Long> existsGenreIds = genreList.stream()
			.map(Genre::getId)
			.toList();

		for (Long id : genreIds) { //입력된 선호장르 리스트 중 존재하지 않는 장르가 있으면 예외 발생
			if (!existsGenreIds.contains(id)) {
				throw new NapzakException(GenreErrorCode.GENRE_NOT_FOUND);
			}
		}
		storeRegistrationService.registerGenrePreference(currentMemberId, genreIds);

		List<GenreNameDto> genrePreferenceDto = genreList.stream()
			.map(genre -> GenreNameDto.from(genre.getId(), genre.getName()))
			.toList();

		GenreNameListResponse response = GenreNameListResponse.fromWithoutCursor(genrePreferenceDto);
		return ResponseEntity.ok()
			.body(SuccessResponse.of(StoreSuccessCode.GENRE_PREPERENCE_REGISTER_SUCCESS, response));
	}

	private List<GenreNameDto> genrePreferenceResponseGenerator(List<GenrePreference> genreList) {

		List<Long> genreIds = genreList.stream().map(GenrePreference::getGenreId).toList();
		Map<Long, String> genreNamesMap = storeGenreFacade.getGenreNames(genreIds);

		return genreList.stream()
			.map(genrePreference -> GenreNameDto.from(genrePreference.getGenreId(),
				genreNamesMap.getOrDefault(genrePreference.getGenreId(), "기타")))
			.toList();
	}

}
