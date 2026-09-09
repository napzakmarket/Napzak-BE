package com.napzak.api.domain.landing.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "App Download", description = "납작마켓 앱 다운로드 랜딩페이지 API")
@RestController
public class AppDownloadPageController {

	private static final String APP_DOWNLOAD_PAGE_PATH =
		"templates/download/app-download-page.html";

	@Operation(
		summary = "앱 다운로드 랜딩페이지 조회",
		description = """
            납작마켓 앱 다운로드를 위한 랜딩페이지 HTML을 반환합니다.
            페이지 접속 시 앱을 자동으로 실행하지 않으며,
            사용자가 다운로드 버튼을 클릭하면 스토어 연결 링크로 이동합니다.
            """
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "앱 다운로드 랜딩페이지 HTML 반환 성공"
		),
		@ApiResponse(
			responseCode = "500",
			description = "랜딩페이지 HTML 로드 실패"
		)
	})
	@GetMapping(
		value = "/download",
		produces = MediaType.TEXT_HTML_VALUE
	)
	public ResponseEntity<String> getAppDownloadPage() throws IOException {
		ClassPathResource resource =
			new ClassPathResource(APP_DOWNLOAD_PAGE_PATH);

		String html = new String(
			resource.getInputStream().readAllBytes(),
			StandardCharsets.UTF_8
		);

		return ResponseEntity.ok()
			.contentType(MediaType.TEXT_HTML)
			.body(html);
	}
}