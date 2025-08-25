package com.napzak.domain.store.crud.genrepreference;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.store.repository.GenrePreferenceRepository;

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
		genrePreferenceRepository.bulkInsert(currentStoreId, genrePreferenceList);
	}
}