package com.napzak.api.domain.genre.dto.response;

import com.napzak.domain.genre.vo.Genre;

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