package com.napzak.api.domain.genre.dto.response;

import java.util.List;

import javax.annotation.Nullable;

import com.napzak.api.domain.genre.service.GenrePagination;
import com.napzak.domain.genre.entity.enums.GenreSortOption;

public record GenreListResponse(
	List<GenreDto> genreList,
	@Nullable
	String nextCursor
) {
	public static GenreListResponse from(
		GenreSortOption genreSortOption,
		GenrePagination pagination
	) {
		List<GenreDto> genreDtos = pagination.getGenreList().stream()
			.map(genre -> {
				return GenreDto.from(genre);
			}).toList();

		String nextCursor = pagination.generateNextCursor(genreSortOption);

		return new GenreListResponse(genreDtos, nextCursor);
	}

	public List<GenreDto> getGenreList() {
		return genreList;
	}

	public String getNextCursor() {
		return nextCursor;
	}
}
