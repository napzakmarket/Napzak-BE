package com.napzak.domain.genre.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.napzak.domain.genre.core.GenreRetriever;
import com.napzak.domain.genre.core.vo.Genre;
import com.napzak.domain.genre.core.vo.GenreList;
import com.napzak.domain.genre.api.service.enums.SortOption;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreService {
	private final GenreRetriever genreRetriever;

	public GenrePagination getGenres(SortOption sortOption, Long cursorGenreId, int size) {
		List<Genre> genres = genreRetriever.retrieveGenres(sortOption, cursorGenreId, size);

		return new GenrePagination(size, new GenreList(genres));
	}

	public GenrePagination searchGenres(String searchWord, SortOption sortOption, Long cursorGenreId, int size) {
		List<Genre> genres = genreRetriever.searchGenres(searchWord, sortOption, cursorGenreId, size);

		return new GenrePagination(size, new GenreList(genres));
	}

	public Genre searchGenre(long genreId) {
		return genreRetriever.findByGenreId(genreId);
	}
}
