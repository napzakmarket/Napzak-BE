package com.napzak.api.domain.product.dto.response;

public record RecommendGenreDto(
	long genreId,
	String genreName,
	String genrePhoto
) {
	public static RecommendGenreDto from(
		long genreId,
		String genreName,
		String genrePhoto
	) {
		return new RecommendGenreDto(genreId, genreName, genrePhoto);
	}
}
