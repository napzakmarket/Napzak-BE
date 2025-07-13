package com.napzak.api.domain.genre.controller;

import com.napzak.api.domain.genre.dto.response.GenreListResponse;
import com.napzak.common.auth.annotation.CurrentMember;
import com.napzak.common.exception.dto.SuccessResponse;
import com.napzak.common.swagger.annotation.DisableSwaggerSecurity;
import com.napzak.api.domain.genre.dto.response.GenreNameListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Genre", description = "장르 관련 API")
public interface GenreApi {

	@DisableSwaggerSecurity
	@Operation(summary = "장르 목록 조회", description = "커서 기반으로 장르 목록을 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "장르 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = GenreListResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@GetMapping("/api/v1/onboarding/genres")
	ResponseEntity<SuccessResponse<GenreListResponse>> getGenres(
		@Parameter(description = "페이징 커서 (예: O-40)")
		@RequestParam(required = false) String cursor,

		@Parameter(description = "조회할 데이터 크기 (기본값: 39)")
		@RequestParam(defaultValue = "39") int size
	);

	@DisableSwaggerSecurity
	@Operation(summary = "장르 검색", description = "검색어 기반으로 장르를 검색하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "검색된 장르 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = GenreListResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@GetMapping("/api/v1/onboarding/genres/search")
	ResponseEntity<SuccessResponse<GenreListResponse>> searchGenres(
		@Parameter(description = "검색할 단어 (예: 은혼)")
		@RequestParam String searchWord,

		@Parameter(description = "페이징 커서 (예: O-40)")
		@RequestParam(required = false) String cursor,

		@Parameter(description = "조회할 데이터 크기 (기본값: 39)")
		@RequestParam(defaultValue = "39") int size
	);

	@Operation(summary = "장르 이름 목록 조회", description = "장르 이름만 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "장르 이름 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = GenreNameListResponse.class))),
		@ApiResponse(responseCode = "404", description = "장르를 찾을 수 없습니다.")
	})
	@GetMapping("/api/v1/genres")
	ResponseEntity<SuccessResponse<GenreNameListResponse>> getGenreNames(
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "43") int size,
		@CurrentMember Long storeId
	);

	@Operation(summary = "장르 이름 검색", description = "검색어 기반으로 장르 이름을 검색하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "검색된 장르 이름 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = GenreNameListResponse.class))),
		@ApiResponse(responseCode = "404", description = "장르를 찾을 수 없습니다.")
	})
	@GetMapping("/api/v1/genres/search")
	ResponseEntity<SuccessResponse<GenreNameListResponse>> searchGenreNames(
		@Parameter(description = "검색할 단어 (예: 은혼)")
		@RequestParam String searchWord,

		@Parameter(description = "페이징 커서 (예: O-40)")
		@RequestParam(required = false) String cursor,

		@Parameter(description = "조회할 데이터 크기 (기본값: 43)")
		@RequestParam(defaultValue = "43") int size,

		@CurrentMember Long storeId
	);
}
