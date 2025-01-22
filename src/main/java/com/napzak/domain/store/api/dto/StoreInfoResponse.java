package com.napzak.domain.store.api.dto;

import java.util.List;

public record StoreInfoResponse(
	Long storeId,
	String storeNickname,
	String storeDescription,
	String storePhoto,
	String storeCover,
	List<GenrePreferenceDto> genrePreferences
) {
	public static StoreInfoResponse of(
		final Long storeId,
		final String storeNickname,
		final String storeDescription,
		final String storePhoto,
		final String storeCover,
		final List<GenrePreferenceDto> genrePreferenceDto
	) {
		return new StoreInfoResponse(storeId, storeNickname, storeDescription, storePhoto, storeCover,
			genrePreferenceDto);
	}
}