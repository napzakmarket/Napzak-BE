package com.napzak.api.domain.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "App Link Verification", description = "iOS Universal Link / Android App Link 검증 파일 API")
@RestController
public class AppLinkVerificationController {

	private final String iosTeamId;
	private final String iosBundleId;
	private final String androidPackageName;
	private final List<String> androidSha256CertFingerprints;

	public AppLinkVerificationController(
		@Value("${napzak.app-link.ios.team-id}") String iosTeamId,
		@Value("${napzak.app-link.ios.bundle-id}") String iosBundleId,
		@Value("${napzak.app-link.android.package-name}") String androidPackageName,
		@Value("${napzak.app-link.android.sha256-cert-fingerprints}") String androidSha256CertFingerprints
	) {
		this.iosTeamId = iosTeamId;
		this.iosBundleId = iosBundleId;
		this.androidPackageName = androidPackageName;
		this.androidSha256CertFingerprints = Arrays.stream(androidSha256CertFingerprints.split(","))
			.map(String::trim)
			.filter(fingerprint -> !fingerprint.isBlank())
			.toList();
	}

	@Operation(
		summary = "iOS Universal Link 검증 파일 조회",
		description = """
			iOS Universal Link 검증을 위한 apple-app-site-association 파일을 반환합니다.

			주의:
			- /api/v1 prefix를 붙이면 안 됩니다.
			- 확장자 없이 /.well-known/apple-app-site-association 경로로 제공해야 합니다.
			- HTTPS로 접근 가능해야 합니다.
			"""
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "iOS Universal Link 검증 파일 반환 성공",
			content = @Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = Map.class),
				examples = @ExampleObject(
					name = "apple-app-site-association",
					value = """
						{
						  "applinks": {
						    "apps": [],
						    "details": [
						      {
						        "appID": "ABCDE12345.com.napzak.market",
						        "paths": ["/product/*"]
						      }
						    ]
						  }
						}
						"""
				)
			)
		),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@GetMapping(
		value = "/.well-known/apple-app-site-association",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Map<String, Object>> getAppleAppSiteAssociation() {
		Map<String, Object> response = Map.of(
			"applinks", Map.of(
				"apps", List.of(),
				"details", List.of(
					Map.of(
						"appID", iosTeamId + "." + iosBundleId,
						"paths", List.of("/product/*")
					)
				)
			)
		);

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(response);
	}

	@Operation(
		summary = "Android App Link 검증 파일 조회",
		description = """
			Android App Link 검증을 위한 assetlinks.json 파일을 반환합니다.

			주의:
			- /api/v1 prefix를 붙이면 안 됩니다.
			- /.well-known/assetlinks.json 경로로 제공해야 합니다.
			- HTTPS로 접근 가능해야 합니다.
			"""
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "Android App Link 검증 파일 반환 성공",
			content = @Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = List.class),
				examples = @ExampleObject(
					name = "assetlinks.json",
					value = """
						[
						  {
						    "relation": [
						      "delegate_permission/common.handle_all_urls"
						    ],
						    "target": {
						      "namespace": "android_app",
						      "package_name": "com.napzak.market",
						      "sha256_cert_fingerprints": [
						        "AA:BB:CC:DD:EE:FF:00:11:22:33:44:55:66:77:88:99:AA:BB:CC:DD:EE:FF:00:11:22:33:44:55:66:77:88:99"
						      ]
						    }
						  }
						]
						"""
				)
			)
		),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@GetMapping(
		value = "/.well-known/assetlinks.json",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<List<Map<String, Object>>> getAssetLinks() {
		List<Map<String, Object>> response = List.of(
			Map.of(
				"relation", List.of("delegate_permission/common.handle_all_urls"),
				"target", Map.of(
					"namespace", "android_app",
					"package_name", androidPackageName,
					"sha256_cert_fingerprints", androidSha256CertFingerprints
				)
			)
		);

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(response);
	}
}