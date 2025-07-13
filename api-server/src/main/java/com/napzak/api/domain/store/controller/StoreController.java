package com.napzak.api.domain.store.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.api.domain.store.StoreChatFacade;
import com.napzak.api.domain.store.StoreInterestFacade;
import com.napzak.api.domain.store.StoreLinkFacade;
import com.napzak.api.domain.store.StoreProductFacade;
import com.napzak.api.domain.store.StoreTermsBundleFacade;
import com.napzak.api.domain.store.dto.request.GenrePreferenceRequest;
import com.napzak.api.domain.store.dto.request.NicknameRequest;
import com.napzak.api.domain.store.dto.request.RoleDto;
import com.napzak.api.domain.store.dto.request.StoreProfileModifyRequest;
import com.napzak.api.domain.store.dto.request.StoreReportRequest;
import com.napzak.api.domain.store.dto.request.StoreWithdrawRequest;
import com.napzak.api.domain.store.dto.response.AccessTokenGenerateResponse;
import com.napzak.api.domain.store.dto.response.LoginSuccessResponse;
import com.napzak.api.domain.store.dto.response.MyPageResponse;
import com.napzak.api.domain.store.dto.response.OnboardingTermsListResponse;
import com.napzak.api.domain.store.dto.response.SettingLinkResponse;
import com.napzak.api.domain.store.dto.response.StoreInfoResponse;
import com.napzak.api.domain.store.dto.response.StoreProfileModifyResponse;
import com.napzak.api.domain.store.dto.response.StoreReportResponse;
import com.napzak.api.domain.store.dto.response.StoreWithdrawResponse;
import com.napzak.api.domain.store.dto.response.TermsDto;
import com.napzak.domain.chat.entity.enums.MessageType;
import com.napzak.domain.chat.entity.enums.SystemMessageType;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.external.entity.enums.LinkType;
import com.napzak.domain.external.entity.enums.TermsType;
import com.napzak.domain.external.vo.UseTerms;
import com.napzak.api.domain.genre.dto.response.GenreNameDto;
import com.napzak.api.domain.genre.dto.response.GenreNameListResponse;
import com.napzak.domain.genre.code.GenreErrorCode;
import com.napzak.domain.genre.vo.Genre;
import com.napzak.domain.product.entity.enums.TradeType;
import com.napzak.api.domain.store.StoreGenreFacade;
import com.napzak.api.domain.store.StoreUseTermsFacade;
import com.napzak.domain.store.code.StoreErrorCode;
import com.napzak.api.domain.store.code.StoreSuccessCode;
import com.napzak.api.domain.store.service.AuthenticationService;
import com.napzak.api.domain.store.service.LoginService;
import com.napzak.api.domain.store.service.StorePhotoS3ImageCleaner;
import com.napzak.api.domain.store.service.StoreRegistrationService;
import com.napzak.api.domain.store.service.StoreService;
import com.napzak.common.auth.annotation.CurrentMember;
import com.napzak.common.auth.client.dto.StoreSocialLoginRequest;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.domain.store.vo.GenrePreference;
import com.napzak.domain.store.vo.Store;
import com.napzak.api.domain.store.service.TokenService;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.dto.SuccessResponse;

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
	private final StoreRegistrationService storeRegistrationService;
	private final AuthenticationService authenticationService;
	private final StorePhotoS3ImageCleaner storePhotoS3ImageCleaner;
	private final StoreGenreFacade storeGenreFacade;
	private final StoreProductFacade storeProductFacade;
	private final StoreLinkFacade storeLinkFacade;
	private final StoreUseTermsFacade storeUseTermsFacade;
	private final StoreTermsBundleFacade storeTermsBundleFacade;
	private final StoreInterestFacade storeInterestFacade;
	private final StoreChatFacade storeChatFacade;

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
		int activeBundleId = storeTermsBundleFacade.findActiveBundleId();

		String noticeLink = storeLinkFacade.findByLinkType(LinkType.NOTICE).getUrl();
		String termsLink = storeUseTermsFacade.findByTermsTypeAndBundleId(TermsType.TERMS, activeBundleId).getTermsUrl();
		String privacyPolicyLink = storeUseTermsFacade.findByTermsTypeAndBundleId(TermsType.PRIVACY_POLICY, activeBundleId).getTermsUrl();
		String versionInfoLink = storeLinkFacade.findByLinkType(LinkType.VERSION_INFO).getUrl();

		SettingLinkResponse settingLinkResponse = SettingLinkResponse.from(noticeLink, termsLink, privacyPolicyLink, versionInfoLink);

		return ResponseEntity.ok().body(SuccessResponse.of(StoreSuccessCode.GET_SETTING_LINK_SUCCESS, settingLinkResponse));
	}

	@GetMapping("/terms")
	public ResponseEntity<SuccessResponse<OnboardingTermsListResponse>> getOnboardingTerms() {
		int activeBundleId = storeTermsBundleFacade.findActiveBundleId();
		UseTerms terms = storeUseTermsFacade.findByTermsTypeAndBundleId(TermsType.TERMS, activeBundleId);
		UseTerms privacyPolicy = storeUseTermsFacade.findByTermsTypeAndBundleId(TermsType.PRIVACY_POLICY, activeBundleId);

		List<TermsDto> termsList = List.of(
			TermsDto.from(terms.getId(), "(필수) 이용약관", terms.getTermsUrl(), terms.isRequired()),
			TermsDto.from(privacyPolicy.getId(), "(필수) 개인정보처리방침", privacyPolicy.getTermsUrl(), terms.isRequired())
		);

		OnboardingTermsListResponse onboardingTermsListResponse = OnboardingTermsListResponse.from(activeBundleId, termsList);

		return ResponseEntity.ok(
			SuccessResponse.of(StoreSuccessCode.GET_ONBOARDING_TERMS_SUCCESS, onboardingTermsListResponse));
	}

	@PostMapping("/terms/{bundleId}")
	public ResponseEntity<SuccessResponse<Void>> registerAgreement(
		@PathVariable("bundleId") int bundleId,
		@CurrentMember final Long currentStoreId
	) {
		storeService.registerAgreement(currentStoreId, bundleId);

		return ResponseEntity.ok(
			SuccessResponse.of(StoreSuccessCode.REGISTER_TERMS_AGREEMENT_SUCCESS));
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
		List<ChatMessage> messages = storeChatFacade.broadcastSystemMessage(storeId, SystemMessageType.WITHDRAWN);
		storeService.withdraw(storeId, request.withdrawTitle(), request.withdrawDescription(), messages);
		storeInterestFacade.deleteInterestByStoreId(storeId);
		storeProductFacade.updateProductIsVisibleByStoreId(storeId);

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

	// 테스트용 삭제 요망
	@PatchMapping("/role/{storeId}")
	public ResponseEntity<SuccessResponse<Void>> changeStoreRole(
		@PathVariable("storeId") Long storeId,
		@RequestBody RoleDto roleDto
	) {
		storeService.changeStoreRole(storeId, roleDto.role());
		return ResponseEntity.ok(SuccessResponse.of(StoreSuccessCode.CHANGE_STORE_ROLE_SUCCESS));
	}

	@PostMapping("/clean")
	public ResponseEntity<SuccessResponse<Void>> storePhotoCleanUp(@CurrentMember Long currentStoreId) {
		Role currentStoreRole = storeService.findRoleByStoreId(currentStoreId);
		if(currentStoreRole != Role.ADMIN) {
			throw new NapzakException(StoreErrorCode.ACCESS_DENIED);
		}
		storePhotoS3ImageCleaner.cleanUnusedStoreImages();

		return ResponseEntity.ok(
			SuccessResponse.of(
				StoreSuccessCode.STORE_PHOTO_DELETE_SUCCESS
			));
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
