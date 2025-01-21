package com.napzak.global.external.s3.api.controller;

import com.napzak.global.common.exception.dto.SuccessResponse;
import com.napzak.global.external.s3.api.dto.ProductPresignedUrlFindAllResponse;
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
	@Operation(summary = "S3 Presigned URL 발급", description = "S3에 업로드할 presigned URL을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Presigned URL 발급 성공",
			content = @Content(schema = @Schema(implementation = ProductPresignedUrlFindAllResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 - productImages 리스트가 비어 있음")
	})
	@GetMapping("/product")
	ResponseEntity<SuccessResponse<ProductPresignedUrlFindAllResponse>> generateAllPresignedUrls(
		@Parameter(description = "업로드할 제품 이미지 리스트", required = true)
		@RequestParam List<String> productImages
	);
}
