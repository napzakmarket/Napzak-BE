package com.napzak.api.domain.genre.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.napzak.api.domain.genre.dto.request.cursor.OldestCursor;
import com.napzak.api.domain.genre.dto.response.GenreListResponse;
import com.napzak.api.domain.genre.service.GenrePagination;
import com.napzak.common.auth.annotation.AuthorizedRole;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.domain.external.entity.enums.LinkType;
import com.napzak.common.auth.annotation.CurrentMember;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.code.ErrorCode;
import com.napzak.common.exception.dto.SuccessResponse;
import com.napzak.api.domain.genre.GenreLinkFacade;
import com.napzak.api.domain.genre.dto.response.GenreNameListResponse;
import com.napzak.api.domain.genre.dto.response.GenrePageResponse;
import com.napzak.api.domain.genre.code.GenreSuccessCode;
import com.napzak.api.domain.genre.service.GenreService;
import com.napzak.domain.genre.entity.enums.GenreSortOption;
import com.napzak.domain.genre.vo.Genre;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class GenreController implements GenreApi {
	private final GenreService genreService;
	private final GenreLinkFacade genreLinkFacade;

	@Override
	@AuthorizedRole({Role.ADMIN, Role.ONBOARDING, Role.STORE, Role.WITHDRAWN})
	@GetMapping("/onboarding/genres")
	public ResponseEntity<SuccessResponse<GenreListResponse>> getGenres(
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "39") int size
	) {
		GenreSortOption genreSortOption = GenreSortOption.OLDEST;

		GenrePagination pagination = genreService.getGenres(
			genreSortOption, parseCursorValues(cursor, genreSortOption), size);

		GenreListResponse response = GenreListResponse.from(genreSortOption, pagination);

		return ResponseEntity.ok(
			SuccessResponse.of(GenreSuccessCode.GENRE_LIST_RETRIEVE_SUCCESS, response)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.ONBOARDING, Role.STORE, Role.WITHDRAWN})
	@GetMapping("/onboarding/genres/search")
	public ResponseEntity<SuccessResponse<GenreListResponse>> searchGenres(
		@RequestParam String searchWord,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "39") int size
	) {
		GenreSortOption genreSortOption = GenreSortOption.OLDEST;

		GenrePagination pagination = genreService.searchGenres(
			searchWord, genreSortOption, parseCursorValues(cursor, genreSortOption), size);

		GenreListResponse response = GenreListResponse.from(genreSortOption, pagination);

		boolean isEmpty = response.genreList().isEmpty();

		String genreLink = isEmpty
			? genreLinkFacade.findByLinkType(LinkType.GENRE_REQUEST).getUrl()
			: null;

		GenreSuccessCode successCode = isEmpty
			? GenreSuccessCode.GENRE_SEARCH_NO_RESULT
			: GenreSuccessCode.GENRE_LIST_SEARCH_SUCCESS;

		return ResponseEntity.ok(
			SuccessResponse.of(successCode, response, genreLink)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("/genres")
	public ResponseEntity<SuccessResponse<GenreNameListResponse>> getGenreNames(
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "43") int size,
		@CurrentMember Long storeId
	) {
		GenreSortOption genreSortOption = GenreSortOption.OLDEST;

		GenrePagination pagination = genreService.getGenres(
			genreSortOption, parseCursorValues(cursor, genreSortOption), size);

		GenreNameListResponse response = GenreNameListResponse.from(genreSortOption, pagination);

		return ResponseEntity.ok(
			SuccessResponse.of(GenreSuccessCode.GENRE_LIST_RETRIEVE_SUCCESS, response)
		);
	}

	@Override
	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("genres/search")
	public ResponseEntity<SuccessResponse<GenreNameListResponse>> searchGenreNames(
		@RequestParam String searchWord,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "43") int size,
		@CurrentMember Long storeId
	) {
		GenreSortOption genreSortOption = GenreSortOption.OLDEST;

		GenrePagination pagination = genreService.searchGenres(
			searchWord, genreSortOption, parseCursorValues(cursor, genreSortOption), size);

		GenreNameListResponse response = GenreNameListResponse.from(genreSortOption, pagination);

		boolean isEmpty = response.genreList().isEmpty();

		String genreLink = isEmpty
			? genreLinkFacade.findByLinkType(LinkType.GENRE_REQUEST).getUrl()
			: null;

		GenreSuccessCode successCode = isEmpty
			? GenreSuccessCode.GENRE_SEARCH_NO_RESULT
			: GenreSuccessCode.GENRE_LIST_SEARCH_SUCCESS;

		return ResponseEntity.ok(
			SuccessResponse.of(successCode, response, genreLink)
		);
	}

	@AuthorizedRole({Role.ADMIN, Role.STORE})
	@GetMapping("genres/detail/{genreId}")
	public ResponseEntity<SuccessResponse<GenrePageResponse>> getGenrePage(
		@CurrentMember Long storeId,
		@PathVariable Long genreId
	) {
		Genre genre = genreService.searchGenre(genreId);
		GenrePageResponse genrePageResponse = GenrePageResponse.from(genre.getId(), genre.getName(), genre.getTag(), genre.getCover());

		return ResponseEntity.ok()
			.body(SuccessResponse.of(GenreSuccessCode.GENRE_PAGE_GET_SUCCESS, genrePageResponse));
	}

	private Long parseCursorValues(String cursor, GenreSortOption genreSortOption) {
		if (cursor == null || cursor.isBlank()) {
			return null;
		}
		if (Objects.requireNonNull(genreSortOption) == GenreSortOption.OLDEST) {
			return OldestCursor.fromString(cursor).getId();
		}
		throw new NapzakException(ErrorCode.INVALID_SORT_OPTION);
	}
}
