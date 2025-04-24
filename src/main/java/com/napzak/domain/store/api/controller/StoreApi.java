package com.napzak.domain.store.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.napzak.domain.genre.api.dto.response.GenreNameListResponse;
import com.napzak.domain.store.api.dto.request.GenrePreferenceRequest;
import com.napzak.domain.store.api.dto.request.NicknameRequest;
import com.napzak.domain.store.api.dto.response.AccessTokenGenerateResponse;
import com.napzak.domain.store.api.dto.response.MyPageResponse;
import com.napzak.domain.store.api.dto.response.StoreInfoResponse;
import com.napzak.domain.store.api.dto.response.StoreLoginResponse;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.auth.client.dto.StoreSocialLoginRequest;
import com.napzak.global.common.exception.dto.SuccessResponse;
import com.napzak.global.swagger.annotation.DisableSwaggerSecurity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Tag(name = "Store", description = "스토어 관련 API")
@RequestMapping("api/v1/stores")
public interface StoreApi {

	@DisableSwaggerSecurity
	@Operation(summary = "스토어 로그인", description = "소셜 로그인 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공",
			content = @Content(schema = @Schema(implementation = StoreLoginResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@PostMapping("/login")
	ResponseEntity<SuccessResponse<StoreLoginResponse>> login(
		@Parameter(description = "소셜 인증 코드")
		@RequestParam("authorizationCode") String authorizationCode,

		@RequestBody StoreSocialLoginRequest storeSocialLoginRequest,

		HttpServletResponse httpServletResponse
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

	@Operation(summary = "마이페이지 조회", description = "마이페이지 정보 조회 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "마이페이지 조회 성공",
			content = @Content(schema = @Schema(implementation = MyPageResponse.class))),
		@ApiResponse(responseCode = "404", description = "스토어를 찾을 수 없습니다.")
	})
	@GetMapping("/mypage")
	ResponseEntity<SuccessResponse<MyPageResponse>> getMyPageInfo(@CurrentMember Long currentStoreId);

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
	@PostMapping("/register")
	ResponseEntity<SuccessResponse<GenreNameListResponse>> register(
		@CurrentMember Long currentMemberId,

		@Valid @RequestBody GenrePreferenceRequest genrePreferenceList
	);

	@Operation(summary = "비속어 Redis 동기화", description = "DB의 비속어 목록을 Redis로 동기화합니다. (관리자 전용)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Redis 비속어 목록 동기화 성공")
	})
	@PostMapping("/admin/sync-redis/slangs")
	ResponseEntity<SuccessResponse<Void>> syncSlangToRedis(@CurrentMember Long currentStoreId);

}
