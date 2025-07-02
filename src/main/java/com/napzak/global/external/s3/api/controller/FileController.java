package com.napzak.global.external.s3.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.global.common.exception.dto.SuccessResponse;
import com.napzak.global.external.s3.api.dto.ChatPresignedUrlFindAllResponse;
import com.napzak.global.external.s3.api.dto.ProductPresignedUrlFindAllResponse;
import com.napzak.global.external.s3.api.dto.StorePresignedUrlFindAllResponse;
import com.napzak.global.external.s3.api.service.FileService;
import com.napzak.global.external.s3.exception.FileSuccessCode;

import feign.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/presigned-url")
@RequiredArgsConstructor
public class FileController implements FileApi {

	private final FileService fileService;

	@Override
	@GetMapping("/product")
	public ResponseEntity<SuccessResponse<ProductPresignedUrlFindAllResponse>> generatePresignedUrlsForProduct(
		@RequestParam List<String> productImages) {
		if (productImages == null || productImages.isEmpty()) {
			throw new IllegalArgumentException("productImages 리스트는 비어 있을 수 없습니다.");
		}
		Map<String, String> urls = fileService.generateAllPresignedUrls(productImages, "product");
		ProductPresignedUrlFindAllResponse response = ProductPresignedUrlFindAllResponse.from(urls);
		return ResponseEntity.ok(
			SuccessResponse.of(FileSuccessCode.PRESIGNED_URL_ISSUED, response));
	}

	@GetMapping("/stores")
	public ResponseEntity<SuccessResponse<StorePresignedUrlFindAllResponse>> generatePresignedUrlsForStore(
		@RequestParam List<String> profileImages) {
		if (profileImages == null || profileImages.isEmpty()) {
			throw new IllegalArgumentException("productImages 리스트는 비어 있을 수 없습니다.");
		}
		Map<String, String> urls = fileService.generateAllPresignedUrls(profileImages, "store");
		StorePresignedUrlFindAllResponse response = StorePresignedUrlFindAllResponse.from(urls);
		return ResponseEntity.ok(
			SuccessResponse.of(FileSuccessCode.PRESIGNED_URL_ISSUED, response));
	}

	@Override
	@GetMapping("/chat")
	public ResponseEntity<SuccessResponse<ChatPresignedUrlFindAllResponse>> generatePresignedUrlsForChat(
		@RequestParam List<String> chatImages) {
		if (chatImages == null || chatImages.isEmpty()) {
			throw new IllegalArgumentException("chatImages 리스트는 비어 있을 수 없습니다.");
		}
		Map<String, String> urls = fileService.generateAllPresignedUrls(chatImages, "chat");
		ChatPresignedUrlFindAllResponse response = ChatPresignedUrlFindAllResponse.from(urls);
		return ResponseEntity.ok(
			SuccessResponse.of(FileSuccessCode.PRESIGNED_URL_ISSUED, response));
	}

}
