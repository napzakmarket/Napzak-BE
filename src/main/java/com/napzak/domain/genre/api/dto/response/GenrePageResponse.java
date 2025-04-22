package com.napzak.domain.genre.api.dto.response;

public record GenrePageResponse(
	long genreId,
	String genreName,
	String tag,
	String cover
) {
	public static GenrePageResponse from(
		long genreId,
		String genreName,
		String tag,
		String cover
	) {
		return new GenrePageResponse(genreId, genreName, tag, cover);
	}
}
