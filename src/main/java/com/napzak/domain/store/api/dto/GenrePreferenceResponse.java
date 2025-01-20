package com.napzak.domain.store.api.dto;

public record GenrePreferenceResponse(
	Long genreId,
	String genreName
) {
	public static GenrePreferenceResponse of(
		final Long genreId,
		final String genreName
	) {
		return new GenrePreferenceResponse(genreId, genreName);
	}
}