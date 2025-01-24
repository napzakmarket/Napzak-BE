package com.napzak.domain.store.core;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.store.core.entity.GenrePreferenceEntity;
import com.napzak.domain.store.core.vo.GenrePreference;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenrePreferenceRetriever {

	private final GenrePreferenceRepository genrePreferenceRepository;

	public List<GenrePreference> getGenrePreferences(Long storeId) {
		List<GenrePreferenceEntity> genrePreferenceEntityList = genrePreferenceRepository.findByStoreId(storeId);
		return genrePreferenceEntityList.stream()
			.map(GenrePreference::fromEntity)
			.toList();
	}

	public boolean existsGenrePreference(Long storeId) {
		return genrePreferenceRepository.existsByStoreId(storeId);
	}

	public List<Long> getGenrePreferenceIds(Long storeId) {
		return genrePreferenceRepository.findGenreIdsByStoreId(storeId);
	}
}
