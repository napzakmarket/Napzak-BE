package com.napzak.domain.store.vo;

import com.napzak.domain.store.entity.GenrePreferenceEntity;

import lombok.Getter;

@Getter
public class GenrePreference {
	private final Long id;
	private final Long storeId;
	private final Long genreId;

	public GenrePreference(Long id, Long storeId, Long genreId) {
		this.id = id;
		this.storeId = storeId;
		this.genreId = genreId;
	}

	public static GenrePreference fromEntity(GenrePreferenceEntity genrePreferenceEntity) {
		return new GenrePreference(
			genrePreferenceEntity.getId(),
			genrePreferenceEntity.getStoreId(),
			genrePreferenceEntity.getGenreId()
		);
	}
}