package com.napzak.api.domain.store.dto.response;

import java.util.List;

import com.napzak.api.domain.genre.dto.response.GenreNameDto;

public record StoreProfileModifyResponse(
	String storeCover,
	String storePhoto,
	String storeNickName,
	String storeDescription,
	List<GenreNameDto> preferredGenreList
) {
	public static StoreProfileModifyResponse of(String storeCover, String storePhoto, String storeNickName,
		String storeDescription, List<GenreNameDto> preferredGenreList) {
		return new StoreProfileModifyResponse(storeCover, storePhoto, storeNickName, storeDescription, preferredGenreList);
	}
}
