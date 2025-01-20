package com.napzak.domain.store.api;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.napzak.domain.genre.core.GenreRetriever;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreGenreFacade {

	private final GenreRetriever genreRetriever;

	public String getGenreName(Long genreId) {
		return genreRetriever.retrieveGenreNameById(genreId);
	}

	public Map<Long, String> getGenreNames(List<Long> genreIds) {
		return genreRetriever.retrieveGenreNamesByIds(genreIds);
	}

	public boolean existById(Long genreId) {
		return genreRetriever.existById(genreId);
	}
}