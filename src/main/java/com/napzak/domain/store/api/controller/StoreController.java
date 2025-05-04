package com.napzak.domain.store.api.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.domain.external.core.entity.enums.LinkType;
import com.napzak.domain.external.core.vo.Link;
import com.napzak.domain.genre.api.dto.response.GenreNameDto;
import com.napzak.domain.genre.api.dto.response.GenreNameListResponse;
import com.napzak.domain.genre.api.exception.GenreErrorCode;
import com.napzak.domain.genre.core.vo.Genre;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.store.api.StoreGenreFacade;
import com.napzak.domain.store.api.StoreLinkFacade;
import com.napzak.domain.store.api.StoreProductFacade;
import com.napzak.domain.store.api.dto.request.GenrePreferenceRequest;
import com.napzak.domain.store.api.dto.request.NicknameRequest;
import com.napzak.domain.store.api.dto.request.StoreProfileModifyRequest;
import com.napzak.domain.store.api.dto.request.StoreReportRequest;
import com.napzak.domain.store.api.dto.request.StoreWithdrawRequest;
import com.napzak.domain.store.api.dto.response.AccessTokenGenerateResponse;
import com.napzak.domain.store.api.dto.response.LoginSuccessResponse;
import com.napzak.domain.store.api.dto.response.MyPageResponse;
import com.napzak.domain.store.api.dto.response.OnboardingTermsListResponse;
import com.napzak.domain.store.api.dto.response.SettingLinkResponse;
import com.napzak.domain.store.api.dto.response.StoreInfoResponse;
import com.napzak.domain.store.api.dto.response.StoreLoginResponse;
import com.napzak.domain.store.api.dto.response.StoreProfileModifyResponse;
import com.napzak.domain.store.api.dto.response.StoreReportResponse;
import com.napzak.domain.store.api.dto.response.StoreWithdrawResponse;
import com.napzak.domain.store.api.dto.response.TermsDto;
import com.napzak.domain.store.api.exception.StoreErrorCode;
import com.napzak.domain.store.api.exception.StoreSuccessCode;
import com.napzak.domain.store.api.service.AuthenticationService;
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
	private final StoreProductFacade storeProductFacade;
	private final StoreLinkFacade storeLinkFacade;
	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<SuccessResponse<LoginSuccessResponse>> login(
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


		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(SuccessResponse.of(StoreSuccessCode.LOGIN_SUCCESS, successResponse));
	}


	@PostMapping("/login/kakao")
	public ResponseEntity<SuccessResponse<LoginSuccessResponse>> loginWithKakaoAccessToken(
		@RequestParam("accessToken") String accessToken,
		@RequestBody StoreSocialLoginRequest storeSocialLoginRequest
	) {
		LoginSuccessResponse loginSuccessResponse = loginService.loginWithAccessToken(accessToken, storeSocialLoginRequest);
		ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, loginSuccessResponse.refreshToken())
			.maxAge(COOKIE_MAX_AGE)
			.path("/")
			.secure(true)
			.sameSite("None")
			.httpOnly(true)
			.build();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(SuccessResponse.of(StoreSuccessCode.LOGIN_SUCCESS, loginSuccessResponse));
	}

	@PostMapping("/logout")
	public ResponseEntity<SuccessResponse<Void>> logOut(
		@CurrentMember final Long currentStoreId
	) {
		tokenService.deleteRefreshToken(currentStoreId);
		return ResponseEntity.ok().body(SuccessResponse.of(StoreSuccessCode.LOGOUT_SUCCESS, null));
	}

	@PostMapping("refresh-token")
	public ResponseEntity<SuccessResponse<AccessTokenGenerateResponse>> reissue(
		@CookieValue("refreshToken") String refreshToken
	) {
		AccessTokenGenerateResponse accessTokenGenerateResponse = authenticationService.generateAccessTokenFromRefreshToken(refreshToken);
		return ResponseEntity.ok(SuccessResponse.of(StoreSuccessCode.ACCESS_TOKEN_REISSUE_SUCCESS, accessTokenGenerateResponse));
	}

	@PostMapping("/nickname/check")
	public ResponseEntity<SuccessResponse<Void>> checkNickname(
		@RequestBody @Valid NicknameRequest request
	) {
		storeService.validateNickname(request.nickname());
		return ResponseEntity.ok(SuccessResponse.of(StoreSuccessCode.VALID_NICKNAME_SUCCESS));
	}

	@PostMapping("/nickname/register")
	public ResponseEntity<SuccessResponse<Void>> registerNickname(
		@CurrentMember final Long currentStoreId,
		@RequestBody @Valid final NicknameRequest request
	) {
		storeService.registerNickname(currentStoreId, request.nickname());
		return ResponseEntity.ok(SuccessResponse.of(StoreSuccessCode.NICKNAME_REGISTER_SUCCESS));
	}

	@GetMapping("/modify/profile")
	public ResponseEntity<SuccessResponse<StoreProfileModifyResponse>> getProfileForModify(
		@CurrentMember final Long currentStoreId
	) {
		Store store = storeService.getStore(currentStoreId);
		List<GenrePreference> genreList = storeService.getGenrePreferenceList(currentStoreId);
		List<GenreNameDto> genrePreferenceDto = genrePreferenceResponseGenerator(genreList);

		StoreProfileModifyResponse response = StoreProfileModifyResponse.of(
			store.getCover(), store.getPhoto(), store.getNickname(), store.getDescription(), genrePreferenceDto
		);
		return ResponseEntity.ok(SuccessResponse.of(StoreSuccessCode.GET_PROFILE_SUCCESS, response));
	}

	@PutMapping("/modify/profile")
	public ResponseEntity<SuccessResponse<StoreProfileModifyResponse>> modifyProfile(
		@CurrentMember final Long currentStoreId,
		@RequestBody @Valid final StoreProfileModifyRequest request
	) {
		List<Genre> genreList = validateAndFindGenres(request.preferredGenreList());

		storeService.modifyProfile(currentStoreId, request.storeCover(), request.storePhoto(), request.storeNickName(), request.storeDescription());
		storeRegistrationService.registerGenrePreference(currentStoreId, request.preferredGenreList());

		List<GenreNameDto> genrePreferenceDto = genreList.stream()
			.map(genre -> GenreNameDto.from(genre.getId(), genre.getName()))
			.toList();

		Store store = storeService.getStore(currentStoreId);
		StoreProfileModifyResponse response = StoreProfileModifyResponse.of(
			store.getCover(), store.getPhoto(), store.getNickname(), store.getDescription(), genrePreferenceDto
		);
		return ResponseEntity.ok(SuccessResponse.of(StoreSuccessCode.PROFILE_UPDATE_SUCCESS, response));
	}

	@GetMapping("/mypage")
	public ResponseEntity<SuccessResponse<MyPageResponse>> getMyPageInfo(
		@CurrentMember final Long currentStoreId
	) {
		Store store = storeService.getStore(currentStoreId);
		int totalSellCount = storeProductFacade.getProductCount(currentStoreId, TradeType.SELL);
		int totalBuyCount = storeProductFacade.getProductCount(currentStoreId, TradeType.BUY);
		String serviceLink = storeLinkFacade.findByLinkType(LinkType.CUSTOMER_SUPPORT).getUrl();

		MyPageResponse myPageResponse = MyPageResponse.of(store.getId(), store.getNickname(), store.getPhoto(),
			totalSellCount, totalBuyCount, serviceLink);
		return ResponseEntity.ok().body(SuccessResponse.of(StoreSuccessCode.GET_MYPAGE_INFO_SUCCESS, myPageResponse));
	}

	@GetMapping("/mypage/setting")
	public ResponseEntity<SuccessResponse<SettingLinkResponse>> getSettingLink(
		@CurrentMember final Long currentStoreId
	) {
		String noticeLink = storeLinkFacade.findByLinkType(LinkType.NOTICE).getUrl();
		String termsLink = storeLinkFacade.findByLinkType(LinkType.TERMS).getUrl();
		String privacyPolicyLink = storeLinkFacade.findByLinkType(LinkType.PRIVACY_POLICY).getUrl();
		String versionInfoLink = storeLinkFacade.findByLinkType(LinkType.VERSION_INFO).getUrl();

		SettingLinkResponse settingLinkResponse = SettingLinkResponse.from(noticeLink, termsLink, privacyPolicyLink, versionInfoLink);

		return ResponseEntity.ok().body(SuccessResponse.of(StoreSuccessCode.GET_SETTING_LINK_SUCCESS, settingLinkResponse));
	}

	@GetMapping("/terms")
	public ResponseEntity<SuccessResponse<OnboardingTermsListResponse>> getOnboardingTerms() {
		Link terms = storeLinkFacade.findByLinkType(LinkType.TERMS);
		Link privacyPolicy = storeLinkFacade.findByLinkType(LinkType.PRIVACY_POLICY);

		List<TermsDto> termsList = List.of(
			TermsDto.from(terms.getId(), "(필수) 이용약관", terms.getUrl()),
			TermsDto.from(privacyPolicy.getId(), "(필수) 개인정보처리방침", privacyPolicy.getUrl())
		);

		OnboardingTermsListResponse onboardingTermsListResponse = OnboardingTermsListResponse.from(termsList);

		return ResponseEntity.ok(
			SuccessResponse.of(StoreSuccessCode.GET_ONBOARDING_TERMS_SUCCESS, onboardingTermsListResponse));
	}

	@GetMapping("/{storeId}")
	public ResponseEntity<SuccessResponse<StoreInfoResponse>> getStoreInfo(
		@PathVariable("storeId") Long ownerId,
		@CurrentMember final Long currentStoreId
	) {

		List<GenrePreference> genreList = storeService.getGenrePreferenceList(ownerId);
		Store store = storeService.getStore(ownerId);

		boolean isStoreOwner = ownerId.equals(currentStoreId);

		List<GenreNameDto> genrePreferenceDto = genrePreferenceResponseGenerator(genreList);

		StoreInfoResponse storeInfoResponse = StoreInfoResponse.of(store.getId(), store.getNickname(),
			store.getDescription(), store.getPhoto(), store.getCover(), isStoreOwner, genrePreferenceDto);

		return ResponseEntity.ok().body(SuccessResponse.of(StoreSuccessCode.GET_STORE_INFO_SUCCESS, storeInfoResponse));
	}

	@PostMapping("genres/register")
	public ResponseEntity<SuccessResponse<GenreNameListResponse>> register(
		@CurrentMember final Long currentStoreId,
		@Valid @RequestBody final GenrePreferenceRequest genrePreferenceList
	) {
		List<Genre> genreList = validateAndFindGenres(genrePreferenceList.genreIds());
		storeRegistrationService.registerGenrePreference(currentStoreId, genrePreferenceList.genreIds());

		List<GenreNameDto> genrePreferenceDto = genreList.stream()
			.map(genre -> GenreNameDto.from(genre.getId(), genre.getName()))
			.toList();

		GenreNameListResponse response = GenreNameListResponse.fromWithoutCursor(genrePreferenceDto);
		return ResponseEntity.ok()
			.body(SuccessResponse.of(StoreSuccessCode.GENRE_PREPERENCE_REGISTER_SUCCESS, response));
	}

	@PostMapping("/report/{storeId}")
	public ResponseEntity<SuccessResponse<StoreReportResponse>> reportStore(
		@PathVariable("storeId") final Long reportedStoreId,
		@CurrentMember final Long reporterStoreId,
		@RequestBody @Valid final StoreReportRequest request
	){
		Store reportedStore = storeService.getStore(reportedStoreId);
		storeService.reportStore(
			reporterStoreId, reportedStore, request.reportTitle(),
			request.reportDescription(), request.reportContact()
		);

		StoreReportResponse response = StoreReportResponse.of(
			reporterStoreId,
			reportedStoreId,
			request.reportTitle(),
			request.reportDescription(),
			request.reportContact()
		);

		return ResponseEntity.ok()
			.body(SuccessResponse.of(StoreSuccessCode.STORE_REPORT_SUCCESS, response));
	}

	@PostMapping("/withdraw")
	public ResponseEntity<SuccessResponse<StoreWithdrawResponse>> withdraw(
		@CurrentMember final Long storeId,
		@RequestBody @Valid final StoreWithdrawRequest request
	) {
		storeService.withdraw(storeId, request.withdrawTitle(), request.withdrawDescription());
		StoreWithdrawResponse response = StoreWithdrawResponse.of(
			storeId,
			request.withdrawTitle(),
			request.withdrawDescription()
		);
		return ResponseEntity.ok(SuccessResponse.of(StoreSuccessCode.STORE_WITHDRAW_SUCCESS, response));
	}

	@PostMapping("/admin/sync-redis/slangs")
	public ResponseEntity<SuccessResponse<Void>> syncSlangToRedis(
		@CurrentMember final Long currentStoreId
	) {
		storeService.syncSlangToRedis();
		return ResponseEntity.ok(SuccessResponse.of(StoreSuccessCode.SLANG_REDIS_UPDATE_SUCCESS));
	}

	private List<GenreNameDto> genrePreferenceResponseGenerator(List<GenrePreference> genreList) {

		List<Long> genreIds = genreList.stream().map(GenrePreference::getGenreId).toList();
		Map<Long, String> genreNamesMap = storeGenreFacade.getGenreNames(genreIds);

		return genreList.stream()
			.map(genrePreference -> GenreNameDto.from(genrePreference.getGenreId(),
				genreNamesMap.getOrDefault(genrePreference.getGenreId(), "기타")))
			.toList();
	}

	private List<Genre> validateAndFindGenres(List<Long> genreIds) {
		int maximumGenreCount = 7;
		if (genreIds.size() > maximumGenreCount) {
			throw new NapzakException(StoreErrorCode.INVALID_GENRE_PREFERENCE_COUNT);
		}
		Set<Long> uniqueGenres = new HashSet<>(genreIds);
		if (uniqueGenres.size() != genreIds.size()) {
			throw new NapzakException(StoreErrorCode.DUPLICATE_GENRE_PREFERENCES);
		}
		List<Genre> genreList = storeGenreFacade.findExistingGenreList(genreIds);
		List<Long> existsGenreIds = genreList.stream().map(Genre::getId).toList();
		for (Long id : genreIds) {
			if (!existsGenreIds.contains(id)) {
				throw new NapzakException(GenreErrorCode.GENRE_NOT_FOUND);
			}
		}
		return genreList;
	}

}
