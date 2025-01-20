package com.napzak.domain.store.core;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenrePreferenceRemover {
	private final GenrePreferenceRepository genrePreferenceRepository;

	public void removeGenrePreference(Long storeId) {
		genrePreferenceRepository.deleteByStoreId(storeId);
	}
}
