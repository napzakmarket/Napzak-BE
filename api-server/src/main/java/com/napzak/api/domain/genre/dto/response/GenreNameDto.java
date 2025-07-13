package com.napzak.api.domain.genre.dto.response;

import com.napzak.domain.genre.vo.Genre;

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