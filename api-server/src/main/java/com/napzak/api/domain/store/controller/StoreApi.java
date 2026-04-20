package com.napzak.api.domain.store.controller;

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

import com.napzak.api.domain.genre.dto.response.GenreNameListResponse;
import com.napzak.api.domain.store.dto.request.GenrePreferenceRequest;
import com.napzak.api.domain.store.dto.request.NicknameRequest;
import com.napzak.api.domain.store.dto.request.RoleDto;
import com.napzak.api.domain.store.dto.request.SmsConfirmRequest;
import com.napzak.api.domain.store.dto.request.SmsSendRequest;
import com.napzak.api.domain.store.dto.request.StoreProfileModifyRequest;
import com.napzak.api.domain.store.dto.request.StoreReportRequest;
import com.napzak.api.domain.store.dto.request.StoreWithdrawRequest;
import com.napzak.api.domain.store.dto.response.AccessTokenGenerateResponse;
import com.napzak.api.domain.store.dto.response.LoginSuccessResponse;
import com.napzak.api.domain.store.dto.response.MyPageResponse;
import com.napzak.api.domain.store.dto.response.OnboardingTermsListResponse;
import com.napzak.api.domain.store.dto.response.PhoneVerificationStatusResponse;
import com.napzak.api.domain.store.dto.response.SettingLinkResponse;
import com.napzak.api.domain.store.dto.response.SmsConfirmResponse;
import com.napzak.api.domain.store.dto.response.SmsSendResponse;
import com.napzak.api.domain.store.dto.response.StoreIdResponse;
import com.napzak.api.domain.store.dto.response.StoreInfoResponse;
import com.napzak.api.domain.store.dto.response.StoreProfileModifyResponse;
import com.napzak.api.domain.store.dto.response.StoreReportResponse;
import com.napzak.api.domain.store.dto.response.StoreWithdrawResponse;
import com.napzak.api.domain.store.dto.response.TokensReissueResponse;
import com.napzak.common.auth.annotation.CurrentMember;
import com.napzak.common.auth.client.dto.StoreSocialLoginRequest;
import com.napzak.common.exception.dto.SuccessResponse;
import com.napzak.common.swagger.annotation.DisableSwaggerSecurity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Tag(name = "Store", description = "스토어 관련 API")
@RequestMapping("api/v1/stores")
public interface StoreApi {

	@DisableSwaggerSecurity
	@Operation(summary = "스토어 로그인", description = "소셜 로그인 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공",
			content = @Content(schema = @Schema(implementation = LoginSuccessResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@PostMapping("/login")
	ResponseEntity<SuccessResponse<LoginSuccessResponse>> login(
		@Parameter(description = "소셜 인증 코드")
		@RequestParam("authorizationCode") String authorizationCode,

		@RequestBody StoreSocialLoginRequest storeSocialLoginRequest,

		HttpServletResponse httpServletResponse
	);

	@DisableSwaggerSecurity
	@Operation(summary = "카카오 액세스 토큰 로그인", description = "카카오 Access Token을 이용한 소셜 로그인 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "카카오 로그인 성공",
			content = @Content(schema = @Schema(implementation = LoginSuccessResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "401", description = "유효하지 않은 카카오 Access Token")
	})
	@PostMapping("/login/kakao")
	ResponseEntity<SuccessResponse<LoginSuccessResponse>> loginWithKakaoAccessToken(
		@Parameter(description = "카카오 Access Token")
		@RequestParam("accessToken") String accessToken,

		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "스토어 소셜 로그인 요청", required = true,
			content = @Content(schema = @Schema(implementation = StoreSocialLoginRequest.class))
		)
		@RequestBody StoreSocialLoginRequest storeSocialLoginRequest
	);

	@Operation(summary = "로그아웃", description = "스토어 로그아웃 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그아웃 성공"),
		@ApiResponse(responseCode = "401", description = "권한 없음")
	})
	@PostMapping("/logout")
	ResponseEntity<SuccessResponse<Void>> logOut(@CurrentMember Long currentStoreId);

	@Operation(summary = "액세스 토큰 재발급", description = "Refresh Token을 기반으로 Access Token을 재발급받는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Access Token 재발급 성공",
			content = @Content(schema = @Schema(implementation = AccessTokenGenerateResponse.class))),
		@ApiResponse(responseCode = "401", description = "Refresh Token이 유효하지 않음"),
		@ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음")
	})
	@PostMapping("/refresh-token")
	ResponseEntity<SuccessResponse<AccessTokenGenerateResponse>> reissue(
		@Parameter(description = "HttpOnly Cookie에 담긴 Refresh Token", hidden = true)
		@CookieValue("refreshToken") String refreshToken
	);

	@Operation(summary = "현재 스토어 ID 조회", description = "현재 로그인한 사용자의 스토어 ID를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "스토어 ID 조회 성공",
			content = @Content(schema = @Schema(implementation = StoreIdResponse.class))),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
	})
	@GetMapping("/store-id")
	ResponseEntity<SuccessResponse<StoreIdResponse>> getStoreId(
		@Parameter(hidden = true)
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "닉네임 중복 및 비속어 검증", description = "닉네임의 중복 여부와 비속어 포함 여부를 검증합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "유효한 닉네임"),
		@ApiResponse(responseCode = "400", description = "닉네임에 비속어 포함"),
		@ApiResponse(responseCode = "409", description = "중복된 닉네임")
	})
	@PostMapping("/nickname/check")
	ResponseEntity<SuccessResponse<Void>> checkNickname(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "닉네임 검증 요청", required = true,
			content = @Content(schema = @Schema(implementation = NicknameRequest.class))
		)
		@RequestBody NicknameRequest request
	);

	@Operation(summary = "닉네임 등록", description = "스토어의 닉네임을 등록하고, 역할을 STORE로 설정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "닉네임 등록 성공")
	})
	@PostMapping("/nickname/register")
	ResponseEntity<SuccessResponse<Void>> registerNickname(
		@CurrentMember Long currentStoreId,
		@RequestBody @Valid NicknameRequest request
	);

	@Operation(summary = "상점 프로필 수정용 정보 조회", description = "상점 프로필 수정을 위한 커버, 프로필, 닉네임, 설명, 장르 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "상점 프로필이 성공적으로 조회되었습니다.",
			content = @Content(schema = @Schema(implementation = StoreProfileModifyResponse.class))),
		@ApiResponse(responseCode = "404", description = "스토어를 찾을 수 없습니다.")
	})
	@GetMapping("/modify/profile")
	ResponseEntity<SuccessResponse<StoreProfileModifyResponse>> getProfileForModify(@CurrentMember Long currentStoreId);

	@Operation(summary = "상점 프로필 수정", description = "상점의 커버, 프로필 사진, 닉네임, 설명, 선호 장르를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "상점 프로필이 성공적으로 수정되었습니다.",
			content = @Content(schema = @Schema(implementation = StoreProfileModifyResponse.class))),
		@ApiResponse(responseCode = "400", description = "요청 필드 값이 유효하지 않습니다.")
	})
	@PutMapping("/modify/profile")
	ResponseEntity<SuccessResponse<StoreProfileModifyResponse>> modifyProfile(
		@CurrentMember Long currentStoreId,
		@RequestBody @Valid StoreProfileModifyRequest request
	);

	@Operation(summary = "마이페이지 조회", description = "마이페이지 정보 조회 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "마이페이지 조회 성공",
			content = @Content(schema = @Schema(implementation = MyPageResponse.class))),
		@ApiResponse(responseCode = "404", description = "스토어를 찾을 수 없습니다.")
	})
	@GetMapping("/mypage")
	ResponseEntity<SuccessResponse<MyPageResponse>> getMyPageInfo(@CurrentMember Long currentStoreId);

	@Operation(summary = "마이페이지 설정 링크 조회", description = "공지사항, 이용약관, 개인정보처리방침, 버전 정보 링크를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "설정 링크 조회 성공",
			content = @Content(schema = @Schema(implementation = SettingLinkResponse.class))),
		@ApiResponse(responseCode = "404", description = "링크 또는 약관 정보를 찾을 수 없습니다.")
	})
	@GetMapping("/mypage/setting")
	ResponseEntity<SuccessResponse<SettingLinkResponse>> getSettingLink(
		@Parameter(hidden = true)
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "온보딩 약관 목록 조회", description = "현재 활성화된 약관 번들의 필수 약관 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "온보딩 약관 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = OnboardingTermsListResponse.class))),
		@ApiResponse(responseCode = "404", description = "활성화된 약관 번들을 찾을 수 없습니다.")
	})
	@GetMapping("/terms")
	ResponseEntity<SuccessResponse<OnboardingTermsListResponse>> getOnboardingTerms();

	@Operation(summary = "약관 동의 등록", description = "현재 로그인한 사용자의 특정 약관 번들 동의를 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "약관 동의 등록 성공"),
		@ApiResponse(responseCode = "404", description = "약관 번들을 찾을 수 없습니다.")
	})
	@PostMapping("/terms/{bundleId}")
	ResponseEntity<SuccessResponse<Void>> registerAgreement(
		@Parameter(description = "약관 번들 ID")
		@PathVariable("bundleId") int bundleId,

		@Parameter(hidden = true)
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "스토어 정보 조회", description = "스토어 ID 기반 스토어 정보 조회 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "스토어 정보 조회 성공",
			content = @Content(schema = @Schema(implementation = StoreInfoResponse.class))),
		@ApiResponse(responseCode = "404", description = "스토어를 찾을 수 없습니다.")
	})
	@GetMapping("/{storeId}")
	ResponseEntity<SuccessResponse<StoreInfoResponse>> getStoreInfo(
		@Parameter(description = "스토어 ID")
		@PathVariable("storeId") Long ownerId,

		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "스토어 장르 등록", description = "스토어 선호 장르 등록 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "선호 장르 등록 성공",
			content = @Content(schema = @Schema(implementation = GenreNameListResponse.class))),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 장르 요청")
	})
	@PostMapping("/genres/register")
	ResponseEntity<SuccessResponse<GenreNameListResponse>> register(
		@CurrentMember Long currentMemberId,

		@Valid @RequestBody GenrePreferenceRequest genrePreferenceList
	);

	@Operation(summary = "스토어 신고", description = "특정 스토어를 신고하는 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "스토어 신고 성공",
			content = @Content(schema = @Schema(implementation = StoreReportResponse.class))),
		@ApiResponse(responseCode = "404", description = "스토어를 찾을 수 없습니다.")
	})
	@PostMapping("/report/{storeId}")
	ResponseEntity<SuccessResponse<StoreReportResponse>> reportStore(
		@Parameter(description = "신고 대상 스토어 ID")
		@PathVariable("storeId") Long reportedStoreId,
		@CurrentMember Long reporterStoreId,
		@RequestBody @Valid StoreReportRequest request
	);

	@Operation(summary = "스토어 차단", description = "현재 로그인한 사용자가 특정 스토어를 차단합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "스토어 차단 성공"),
		@ApiResponse(responseCode = "400", description = "자기 자신은 차단할 수 없습니다."),
		@ApiResponse(responseCode = "404", description = "스토어를 찾을 수 없습니다.")
	})
	@PostMapping("/block/{storeId}")
	ResponseEntity<SuccessResponse<Void>> blockStore(
		@Parameter(description = "차단 대상 스토어 ID")
		@PathVariable("storeId") Long opponentStoreId,

		@Parameter(hidden = true)
		@CurrentMember Long myStoreId
	);

	@Operation(summary = "스토어 차단 해제", description = "현재 로그인한 사용자가 특정 스토어 차단을 해제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "스토어 차단 해제 성공"),
		@ApiResponse(responseCode = "404", description = "차단 정보 또는 스토어를 찾을 수 없습니다.")
	})
	@PostMapping("/unblock/{storeId}")
	ResponseEntity<SuccessResponse<Void>> unblockStore(
		@Parameter(description = "차단 해제 대상 스토어 ID")
		@PathVariable("storeId") Long opponentStoreId,

		@Parameter(hidden = true)
		@CurrentMember Long myStoreId
	);


	@Operation(summary = "스토어 탈퇴", description = "스토어 회원 탈퇴를 요청하는 API입니다. 신고 승인된 유저도 요청 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "스토어 탈퇴 성공",
			content = @Content(schema = @Schema(implementation = StoreWithdrawResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@PostMapping("/withdraw")
	ResponseEntity<SuccessResponse<StoreWithdrawResponse>> withdraw(
		@CurrentMember Long storeId,
		@RequestBody @Valid StoreWithdrawRequest request
	);

	@Operation(summary = "비속어 Redis 동기화", description = "DB의 비속어 목록을 Redis로 동기화합니다. (관리자 전용)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Redis 비속어 목록 동기화 성공")
	})
	@PostMapping("/admin/sync-redis/slangs")
	ResponseEntity<SuccessResponse<Void>> syncSlangToRedis(@CurrentMember Long currentStoreId);

	@Operation(summary = "스토어 역할 변경", description = "스토어 역할을 변경합니다. 테스트용 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "스토어 역할 변경 성공"),
		@ApiResponse(responseCode = "404", description = "스토어를 찾을 수 없습니다.")
	})
	@PatchMapping("/role/{storeId}")
	ResponseEntity<SuccessResponse<Void>> changeStoreRole(
		@Parameter(description = "역할을 변경할 스토어 ID")
		@PathVariable("storeId") Long storeId,

		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "변경할 역할 정보", required = true,
			content = @Content(schema = @Schema(implementation = RoleDto.class))
		)
		@RequestBody RoleDto roleDto
	);

	@Operation(summary = "미사용 스토어 이미지 정리", description = "사용되지 않는 스토어 이미지를 정리합니다. 관리자 전용 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "미사용 스토어 이미지 정리 성공"),
		@ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다.")
	})
	@PostMapping("/clean")
	ResponseEntity<SuccessResponse<Void>> storePhotoCleanUp(
		@Parameter(hidden = true)
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "토큰 재발급", description = "기존 Refresh Token을 Authorization 헤더로 전달받아 Access Token과 Refresh Token을 재발급합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
			content = @Content(schema = @Schema(implementation = TokensReissueResponse.class))),
		@ApiResponse(responseCode = "401", description = "유효하지 않은 Authorization 헤더 또는 Refresh Token"),
		@ApiResponse(responseCode = "404", description = "스토어를 찾을 수 없습니다.")
	})
	@PostMapping("/reissue-tokens")
	ResponseEntity<SuccessResponse<TokensReissueResponse>> reissueTokens(
		@Parameter(hidden = true)
		HttpServletRequest request,

		@Parameter(hidden = true)
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "번호 인증 상태 조회", description = "현재 로그인한 사용자의 번호 인증 완료 여부를 조회합니다. 상품 등록 또는 채팅 메시지 발송 전 인증 필요 여부 확인에 사용합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "번호 인증 상태 조회 성공",
			content = @Content(schema = @Schema(implementation = PhoneVerificationStatusResponse.class))),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
		@ApiResponse(responseCode = "404", description = "스토어를 찾을 수 없습니다.")
	})
	@GetMapping("/phone-verification-status")
	ResponseEntity<SuccessResponse<PhoneVerificationStatusResponse>> getPhoneVerificationStatus(
		@Parameter(hidden = true)
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "번호 인증번호 발송", description = "입력한 전화번호로 인증번호를 발송합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "인증번호 발송 성공",
			content = @Content(schema = @Schema(implementation = SmsSendResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 전화번호 형식 또는 일일 발송 한도 초과"),
		@ApiResponse(responseCode = "409", description = "이미 사용 중인 전화번호 또는 이미 인증된 사용자")
	})
	@PostMapping("/phone-verifications/send")
	ResponseEntity<SuccessResponse<SmsSendResponse>> sendVerificationCode(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "인증번호 발송 요청", required = true,
			content = @Content(schema = @Schema(implementation = SmsSendRequest.class))
		)
		@Valid @RequestBody SmsSendRequest request,

		@Parameter(hidden = true)
		@CurrentMember Long currentStoreId
	);

	@Operation(summary = "번호 인증번호 검증", description = "전화번호와 인증번호를 검증하고, 성공 시 현재 스토어의 번호 인증 상태를 완료 처리합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "인증번호 검증 성공",
			content = @Content(schema = @Schema(implementation = SmsConfirmResponse.class))),
		@ApiResponse(responseCode = "400", description = "인증 세션 없음, 인증번호 불일치 또는 검증 실패 횟수 초과"),
		@ApiResponse(responseCode = "409", description = "이미 사용 중인 전화번호 또는 이미 인증된 사용자")
	})
	@PostMapping("/phone-verifications/confirm")
	ResponseEntity<SuccessResponse<SmsConfirmResponse>> confirmVerificationCode(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "인증번호 검증 요청", required = true,
			content = @Content(schema = @Schema(implementation = SmsConfirmRequest.class))
		)
		@Valid @RequestBody SmsConfirmRequest request,

		@Parameter(hidden = true)
		@CurrentMember Long currentStoreId
	);

}
