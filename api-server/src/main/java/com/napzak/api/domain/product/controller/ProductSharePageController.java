package com.napzak.api.domain.product.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.api.domain.product.service.ProductSharePageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Product Share", description = "상품 외부 공유 링크 API")
@RestController
@RequiredArgsConstructor
public class ProductSharePageController {

	private final ProductSharePageService productSharePageService;

	@Operation(
		summary = "상품 공유 링크 랜딩페이지 조회",
		description = """
			외부 공유 링크 접근 시 OG 태그와 앱 설치 랜딩페이지 HTML을 반환합니다.

			- 판매중/판매완료 상품: 상품명, 설명, 대표 이미지 기반 OG 태그 반환
			- 삭제/조회불가 상품: 삭제 상품용 기본 OG 태그와 안내 랜딩페이지 반환
			- 이 API는 JSON이 아니라 text/html을 반환합니다.
			- 삭제/조회불가 상품도 링크 프리뷰 유지를 위해 200 OK HTML을 반환합니다.
			"""
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "상품 공유 랜딩페이지 HTML 반환 성공",
			content = @Content(
				mediaType = MediaType.TEXT_HTML_VALUE,
				schema = @Schema(type = "string"),
				examples = @ExampleObject(
					name = "HTML 응답 예시",
					value = """
						<!DOCTYPE html>
						<html lang="ko">
						<head>
						    <meta property="og:title" content="긴토키 히지카타 룩업 | 납작마켓" />
						    <meta property="og:description" content="은혼 긴토키 히지카타 룩업 팝니다…" />
						    <meta property="og:image" content="https://cdn.example.com/product/123.jpg" />
						    <meta property="og:url" content="https://도메인/product/123" />
						</head>
						<body>
						    <h1>납작마켓 앱에서 상품을 확인해 보세요.</h1>
						</body>
						</html>
						"""
				)
			)
		),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@GetMapping(
		value = "/product/{productId}",
		produces = MediaType.TEXT_HTML_VALUE
	)
	public ResponseEntity<String> getProductSharePage(
		@Parameter(description = "상품 ID", example = "123")
		@PathVariable("productId") Long productId
	) {
		String html = productSharePageService.renderProductSharePage(productId);

		return ResponseEntity.ok()
			.contentType(MediaType.TEXT_HTML)
			.body(html);
	}
}