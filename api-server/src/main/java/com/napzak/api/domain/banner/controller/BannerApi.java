package com.napzak.api.domain.banner.controller;

import com.napzak.api.domain.banner.dto.response.BannerResponseList;
import com.napzak.common.exception.dto.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Banner", description = "배너 관련 API")
@RequestMapping("api/v1/banners")
public interface BannerApi {

	@Operation(summary = "홈 배너 조회", description = "홈 페이지의 배너 목록을 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배너 목록 조회 성공"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/home")
	ResponseEntity<SuccessResponse<BannerResponseList>> getBanners();
}
