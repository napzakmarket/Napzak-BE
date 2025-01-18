package com.napzak.domain.genre.api.dto.response;

import java.util.List;

import javax.annotation.Nullable;

import com.napzak.domain.genre.api.service.GenrePagination;
import com.napzak.domain.genre.api.service.enums.SortOption;

public record GenreListResponse(
	List<GenreDto> genreList,
	@Nullable
	String nextCursor
) {
	public static GenreListResponse from(
		SortOption sortOption,
		GenrePagination pagination
	) {
		List<GenreDto> genreDtos = pagination.getGenreList().stream()
			.map(genre -> {
				return GenreDto.from(genre);
			}).toList();

		String nextCursor = pagination.generateNextCursor(sortOption);

		return new GenreListResponse(genreDtos, nextCursor);
	}

	public List<GenreDto> getGenreList() {
		return genreList;
	}

	public String getNextCursor() {
		return nextCursor;
	}
}
