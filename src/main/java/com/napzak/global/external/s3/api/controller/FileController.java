package com.napzak.global.external.s3.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.global.common.exception.dto.SuccessResponse;
import com.napzak.global.external.s3.api.dto.ProductPresignedUrlFindAllResponse;
import com.napzak.global.external.s3.api.service.FileService;
import com.napzak.global.external.s3.exception.FileSuccessCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/presigned-url")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@GetMapping("/product")
	public ResponseEntity<SuccessResponse<ProductPresignedUrlFindAllResponse>> generateAllPresignedUrls(
		@RequestParam List<String> productImages) {
		if (productImages == null || productImages.isEmpty()) {
			throw new IllegalArgumentException("productImages 리스트는 비어 있을 수 없습니다.");
		}

		ProductPresignedUrlFindAllResponse response = fileService.generateAllPresignedUrlsForProduct(productImages);

		return ResponseEntity.ok(
			SuccessResponse.of(FileSuccessCode.PRESIGNED_URL_ISSUED, response)
		);
	}
}
