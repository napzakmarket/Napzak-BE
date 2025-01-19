package com.napzak.domain.genre.api.dto.response;

import com.napzak.domain.genre.core.vo.Genre;

public record GenreDto(
	Long genreId,
	String genreName,
	String genrePhoto
) {
	public static GenreDto from(Genre genre) {
		return new GenreDto(
			genre.getId(),
			genre.getName(),
			genre.getPhotoUrl()
		);
	}

	public static GenreDto from(
		Long genreId,
		String genreName,
		String genrePhoto
	) {
		return new GenreDto(genreId, genreName, genrePhoto);
	}
}