package com.napzak.api.domain.genre.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.napzak.domain.genre.crud.GenreRetriever;
import com.napzak.domain.genre.vo.Genre;
import com.napzak.domain.genre.vo.GenreList;
import com.napzak.domain.genre.entity.enums.GenreSortOption;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreService {
	private final GenreRetriever genreRetriever;

	public GenrePagination getGenres(GenreSortOption genreSortOption, Long cursorGenreId, int size) {
		List<Genre> genres = genreRetriever.retrieveGenres(genreSortOption, cursorGenreId, size);

		return new GenrePagination(size, new GenreList(genres));
	}

	public GenrePagination searchGenres(String searchWord, GenreSortOption genreSortOption, Long cursorGenreId, int size) {
		List<Genre> genres = genreRetriever.searchGenres(searchWord, genreSortOption, cursorGenreId, size);

		return new GenrePagination(size, new GenreList(genres));
	}

	public Genre searchGenre(long genreId) {
		return genreRetriever.findByGenreId(genreId);
	}
}
