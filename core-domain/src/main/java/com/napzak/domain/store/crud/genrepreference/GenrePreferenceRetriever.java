package com.napzak.domain.store.crud.genrepreference;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.entity.GenrePreferenceEntity;
import com.napzak.domain.store.repository.GenrePreferenceRepository;
import com.napzak.domain.store.vo.GenrePreference;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenrePreferenceRetriever {

	private final GenrePreferenceRepository genrePreferenceRepository;

	@Transactional(readOnly = true)
	public List<GenrePreference> getGenrePreferences(Long storeId) {
		List<GenrePreferenceEntity> genrePreferenceEntityList = genrePreferenceRepository.findByStoreId(storeId);
		return genrePreferenceEntityList.stream()
			.map(GenrePreference::fromEntity)
			.toList();
	}

	@Transactional(readOnly = true)
	public boolean existsGenrePreference(Long storeId) {
		return genrePreferenceRepository.existsByStoreId(storeId);
	}

	@Transactional(readOnly = true)
	public List<Long> getGenrePreferenceIds(Long storeId) {
		return genrePreferenceRepository.findGenreIdsByStoreId(storeId);
	}
}
