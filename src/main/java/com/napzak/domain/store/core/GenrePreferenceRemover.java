package com.napzak.domain.store.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class GenrePreferenceRemover {
	private final GenrePreferenceRepository genrePreferenceRepository;

	public void removeGenrePreference(Long storeId) {
		genrePreferenceRepository.deleteByStoreId(storeId);
	}
}
