package com.napzak.domain.genre.api.dto.response;

import com.napzak.domain.genre.core.vo.Genre;

public record GenreNameDto(
	Long genreId,
	String genreName
) {
	public static GenreNameDto from(Genre genre) {
		return new GenreNameDto(
			genre.getId(),
			genre.getName()
		);
	}

	public static GenreNameDto from(
		Long genreId,
		String genreName
	) {
		return new GenreNameDto(genreId, genreName);
	}
}