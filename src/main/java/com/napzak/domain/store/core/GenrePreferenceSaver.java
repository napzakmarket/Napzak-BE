package com.napzak.domain.store.core;

import org.springframework.stereotype.Component;

import com.napzak.domain.genre.api.exception.GenreErrorCode;
import com.napzak.domain.store.api.StoreGenreFacade;
import com.napzak.domain.store.core.entity.GenrePreferenceEntity;
import com.napzak.global.common.exception.NapzakException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class GenrePreferenceSaver {

	private final GenrePreferenceRepository genrePreferenceRepository;
	private final StoreGenreFacade storeGenreFacade;

	public void save(
		final GenrePreferenceEntity genrePreferenceEntity
	) {
		if (!storeGenreFacade.existById(genrePreferenceEntity.getGenreId())) {
			throw new NapzakException(GenreErrorCode.GENRE_NOT_FOUND);
		}
		genrePreferenceRepository.save(genrePreferenceEntity);
	}
}
