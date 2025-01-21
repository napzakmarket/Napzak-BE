package com.napzak.domain.genre.api.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.domain.genre.api.dto.response.GenreListResponse;
import com.napzak.domain.genre.api.dto.response.GenreNameListResponse;
import com.napzak.domain.genre.api.exception.GenreSuccessCode;
import com.napzak.domain.genre.api.service.GenrePagination;
import com.napzak.domain.genre.api.service.GenreService;
import com.napzak.domain.genre.api.service.enums.SortOption;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.common.exception.NapzakException;
import com.napzak.global.common.exception.code.ErrorCode;
import com.napzak.global.common.exception.dto.SuccessResponse;
import com.napzak.domain.genre.api.dto.request.cursor.OldestCursor;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class GenreController implements GenreApi {
	private final GenreService genreService;

	@Override
	@GetMapping("/onboarding/genres")
	public ResponseEntity<SuccessResponse<GenreListResponse>> getGenres(
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "39") int size
	) {
		SortOption sortOption = SortOption.OLDEST;

		GenrePagination pagination = genreService.getGenres(
			sortOption, parseCursorValues(cursor, sortOption), size);

		GenreListResponse response = GenreListResponse.from(sortOption, pagination);

		return ResponseEntity.ok(
			SuccessResponse.of(GenreSuccessCode.GENRE_LIST_RETRIEVE_SUCCESS, response)
		);
	}

	@Override
	@GetMapping("/onboarding/genres/search")
	public ResponseEntity<SuccessResponse<GenreListResponse>> searchGenres(
		@RequestParam String searchWord,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "39") int size
	) {
		SortOption sortOption = SortOption.OLDEST;

		GenrePagination pagination = genreService.searchGenres(
			searchWord, sortOption, parseCursorValues(cursor, sortOption), size);

		GenreListResponse response = GenreListResponse.from(sortOption, pagination);

		return ResponseEntity.ok(
			SuccessResponse.of(GenreSuccessCode.GENRE_LIST_RETRIEVE_SUCCESS, response)
		);
	}

	@Override
	@GetMapping("/genres")
	public ResponseEntity<SuccessResponse<GenreNameListResponse>> getGenreNames(
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "43") int size,
		@CurrentMember Long storeId
	) {
		SortOption sortOption = SortOption.OLDEST;

		GenrePagination pagination = genreService.getGenres(
			sortOption, parseCursorValues(cursor, sortOption), size);

		GenreNameListResponse response = GenreNameListResponse.from(sortOption, pagination);

		return ResponseEntity.ok(
			SuccessResponse.of(GenreSuccessCode.GENRE_LIST_RETRIEVE_SUCCESS, response)
		);
	}

	@Override
	@GetMapping("genres/search")
	public ResponseEntity<SuccessResponse<GenreNameListResponse>> searchGenreNames(
		@RequestParam String searchWord,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "43") int size,
		@CurrentMember Long storeId
	) {
		SortOption sortOption = SortOption.OLDEST;

		GenrePagination pagination = genreService.searchGenres(
			searchWord, sortOption, parseCursorValues(cursor, sortOption), size);

		GenreNameListResponse response = GenreNameListResponse.from(sortOption, pagination);

		return ResponseEntity.ok(
			SuccessResponse.of(GenreSuccessCode.GENRE_LIST_RETRIEVE_SUCCESS, response)
		);
	}

	private Long parseCursorValues(String cursor, SortOption sortOption) {
		if (cursor == null || cursor.isBlank()) {
			return null;
		}
		if (Objects.requireNonNull(sortOption) == SortOption.OLDEST) {
			return OldestCursor.fromString(cursor).getId();
		}
		throw new NapzakException(ErrorCode.INVALID_SORT_OPTION);
	}
}
