package com.napzak.api.domain.store.dto.response;

import java.util.List;

import com.napzak.api.domain.genre.dto.response.GenreNameDto;

public record StoreInfoResponse(
	Long storeId,
	String storeNickName,
	String storeDescription,
	String storePhoto,
	String storeCover,
	boolean isStoreOwner,
	List<GenreNameDto> genrePreferences
) {
	public static StoreInfoResponse of(
		final Long storeId,
		final String storeNickName,
		final String storeDescription,
		final String storePhoto,
		final String storeCover,
		final boolean isStoreOwner,
		final List<GenreNameDto> GenreNameDto
	) {
		return new StoreInfoResponse(storeId, storeNickName, storeDescription, storePhoto, storeCover,
			isStoreOwner, GenreNameDto);
	}
}