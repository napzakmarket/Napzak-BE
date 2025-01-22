package com.napzak.domain.store.api.dto.response;

import java.util.List;

import com.napzak.domain.genre.api.dto.response.GenreNameDto;

public record StoreInfoResponse(
	Long storeId,
	String storeNickName,
	String storeDescription,
	String storePhoto,
	String storeCover,
	List<GenreNameDto> genrePreferences
) {
	public static StoreInfoResponse of(
		final Long storeId,
		final String storeNickName,
		final String storeDescription,
		final String storePhoto,
		final String storeCover,
		final List<GenreNameDto> GenreNameDto
	) {
		return new StoreInfoResponse(storeId, storeNickName, storeDescription, storePhoto, storeCover,
			GenreNameDto);
	}
}