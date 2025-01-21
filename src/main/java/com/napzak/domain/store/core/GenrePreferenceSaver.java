package com.napzak.domain.store.core;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.store.core.entity.GenrePreferenceEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class GenrePreferenceSaver {

	private final GenrePreferenceRepository genrePreferenceRepository;

	public void save(
		final List<Long> genrePreferenceList,
		final Long currentStoreId
	) {
		genrePreferenceList.forEach(genreId -> {

			GenrePreferenceEntity genrePreferenceEntity = GenrePreferenceEntity.create(currentStoreId, genreId);
			genrePreferenceRepository.save(genrePreferenceEntity);
		});

	}
}