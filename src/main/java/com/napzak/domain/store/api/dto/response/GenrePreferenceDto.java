package com.napzak.domain.store.api.dto.response;

public record GenrePreferenceDto(
	Long genreId,
	String genreName
) {
	public static GenrePreferenceDto of(
		final Long genreId,
		final String genreName
	) {
		return new GenrePreferenceDto(genreId, genreName);
	}
}
