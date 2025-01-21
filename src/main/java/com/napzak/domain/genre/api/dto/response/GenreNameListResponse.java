package com.napzak.domain.genre.api.dto.response;

import java.util.List;

import javax.annotation.Nullable;

import com.napzak.domain.genre.api.service.GenrePagination;
import com.napzak.domain.genre.api.service.enums.SortOption;

public record GenreNameListResponse(
	List<GenreNameDto> genreList,
	@Nullable
	String nextCursor
) {
	public static GenreNameListResponse from(
		SortOption sortOption,
		GenrePagination pagination
	) {
		List<GenreNameDto> genreNameDtos = pagination.getGenreList().stream()
			.map(genre -> {
				return GenreNameDto.from(genre);
			}).toList();

		String nextCursor = pagination.generateNextCursor(sortOption);

		return new GenreNameListResponse(genreNameDtos, nextCursor);
	}

	public static GenreNameListResponse fromWithoutCursor(
		List<GenreNameDto> genreList
	) {
		return new GenreNameListResponse(genreList, null);
	}

	public List<GenreNameDto> getGenreList() {
		return genreList;
	}

	public String getNextCursor() {
		return nextCursor;
	}
}
