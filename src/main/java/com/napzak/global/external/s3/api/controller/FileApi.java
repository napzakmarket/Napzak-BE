package com.napzak.global.external.s3.api.controller;

import com.napzak.global.common.exception.dto.SuccessResponse;
import com.napzak.global.external.s3.api.dto.ProductPresignedUrlFindAllResponse;
import com.napzak.global.external.s3.api.dto.StorePresignedUrlFindAllResponse;
import com.napzak.global.swagger.annotation.DisableSwaggerSecurity;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "S3 File", description = "S3 파일 업로드 관련 API")
@RequestMapping("/api/v1/presigned-url")
public interface FileApi {

	@DisableSwaggerSecurity
	@Operation(summary = "상품 이미지 Presigned URL 발급", description = "상품 이미지 업로드를 위한 Presigned URL을 발급합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Presigned URL 발급 성공",
			content = @Content(schema = @Schema(implementation = ProductPresignedUrlFindAllResponse.class))),
		@ApiResponse(responseCode = "400", description = "이미지 리스트가 비어있음")
	})
	@GetMapping("/product")
	ResponseEntity<SuccessResponse<ProductPresignedUrlFindAllResponse>> generatePresignedUrlsForProduct(
		@RequestParam List<String> productImages);

	@DisableSwaggerSecurity
	@Operation(summary = "상점 이미지 Presigned URL 발급", description = "상점 프로필/커버 업로드용 Presigned URL을 발급합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Presigned URL 발급 성공",
			content = @Content(schema = @Schema(implementation = StorePresignedUrlFindAllResponse.class))),
		@ApiResponse(responseCode = "400", description = "이미지 리스트가 비어있음")
	})
	@GetMapping("/stores")
	ResponseEntity<SuccessResponse<StorePresignedUrlFindAllResponse>> generatePresignedUrlsForStore(
		@RequestParam List<String> profileImages);

}
