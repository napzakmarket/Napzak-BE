package com.napzak.domain.store.api.dto;

import java.util.List;

public record StoreNormalRegisterResponse(
	Long storeId,
	String nickname,
	List<GenrePreferenceResponse> genrePreferenceList
) {
	public static StoreNormalRegisterResponse from(
		final Long storeId,
		final String nickname,
		final List<GenrePreferenceResponse> genrePreferenceList
	) {
		return new StoreNormalRegisterResponse(
			storeId,
			nickname,
			genrePreferenceList
		);
	}
}
