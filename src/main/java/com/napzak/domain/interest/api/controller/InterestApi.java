package com.napzak.domain.interest.api.controller;

import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.common.exception.dto.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Interest", description = "관심 상품 관련 API")
@RequestMapping("api/v1/interest")
public interface InterestApi {

	@Operation(summary = "관심 상품 추가", description = "사용자가 관심 있는 상품을 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "관심 상품 추가 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
	})
	@PostMapping("/{productId}")
	ResponseEntity<SuccessResponse<Void>> postInterest(
		@Parameter(description = "상품 ID", example = "1")
		@PathVariable("productId") Long productId,

		@Parameter(description = "현재 로그인한 회원의 상점 ID")
		@CurrentMember Long storeId
	);

	@Operation(summary = "관심 상품 삭제", description = "사용자가 관심 상품에서 제거합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "관심 상품 삭제 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
	})
	@DeleteMapping("/{productId}")
	ResponseEntity<SuccessResponse<Void>> deleteInterest(
		@Parameter(description = "상품 ID", example = "1")
		@PathVariable("productId") Long productId,

		@Parameter(description = "현재 로그인한 회원의 상점 ID")
		@CurrentMember Long storeId
	);
}
