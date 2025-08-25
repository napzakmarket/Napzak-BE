package com.napzak.api.domain.store;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.napzak.domain.genre.crud.GenreRetriever;
import com.napzak.domain.genre.vo.Genre;

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

	public List<Genre> findExistingGenreList(List<Long> genreIds) {
		return genreRetriever.findExistingGenreList(genreIds);
	}
}
