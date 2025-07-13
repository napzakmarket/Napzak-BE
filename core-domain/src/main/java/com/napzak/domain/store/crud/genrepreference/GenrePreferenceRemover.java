package com.napzak.domain.store.crud.genrepreference;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.repository.GenrePreferenceRepository;

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
