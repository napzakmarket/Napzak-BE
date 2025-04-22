package com.napzak.domain.genre.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.genre.api.exception.GenreErrorCode;
import com.napzak.domain.genre.api.service.enums.SortOption;
import com.napzak.domain.genre.core.entity.GenreEntity;
import com.napzak.domain.genre.core.vo.Genre;
import com.napzak.global.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenreRetriever {
	private final GenreRepository genreRepository;

	@Transactional(readOnly = true)
	public String retrieveGenreNameById(Long genreId) {
		return genreRepository.findNameById(genreId);
	}

	@Transactional(readOnly = true)
	public Map<Long, String> retrieveGenreNamesByIds(List<Long> genreIds) {
		return genreRepository.findNamesByIds(genreIds).stream()
			.collect(Collectors.toMap(
				result -> (Long)result[0],   // Key: Genre ID
				result -> (String)result[1] // Value: Genre Name
			));
	}

	@Transactional(readOnly = true)
	public List<Genre> retrieveGenres(SortOption sortOption, Long cursorGenreId, int size) {

		return genreRepository.getGenresByWithCursor(
				sortOption.toOrderSpecifier(), cursorGenreId, size
			).stream()
			.map(Genre::fromEntity)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<Genre> searchGenres(String searchWord, SortOption sortOption, Long cursorGenreId, int size) {

		return genreRepository.searchGenresBySearchWordWithCursor(
				searchWord, sortOption.toOrderSpecifier(), cursorGenreId, size
			).stream()
			.map(Genre::fromEntity)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<Genre> findExistingGenrList(List<Long> genreIds) {
		return genreRepository.findExistingGenreEntityList(genreIds).stream()
			.map(Genre::fromEntity)
			.toList();
	}

	@Transactional(readOnly = true)
	public Genre findByGenreId(Long genreId) {
		GenreEntity genreEntity = genreRepository.findById(genreId)
			.orElseThrow(()-> new NapzakException(GenreErrorCode.GENRE_NOT_FOUND));
		return Genre.fromEntity(genreEntity);
	}
}
