package com.napzak.api.domain.genre.dto.response;

import java.util.List;

import javax.annotation.Nullable;

import com.napzak.api.domain.genre.service.GenrePagination;
import com.napzak.domain.genre.entity.enums.GenreSortOption;

public record GenreNameListResponse(
	List<GenreNameDto> genreList,
	@Nullable
	String nextCursor
) {
	public static GenreNameListResponse from(
		GenreSortOption genreSortOption,
		GenrePagination pagination
	) {
		List<GenreNameDto> genreNameDtos = pagination.getGenreList().stream()
			.map(genre -> {
				return GenreNameDto.from(genre);
			}).toList();

		String nextCursor = pagination.generateNextCursor(genreSortOption);

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
