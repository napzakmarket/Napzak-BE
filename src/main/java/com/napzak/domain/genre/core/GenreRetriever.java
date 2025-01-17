package com.napzak.domain.genre.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenreRetriever {
	private final GenreRepository genreRepository;

	@Transactional(readOnly = true)
	public String getGenreNameById(Long genreId) {
		return genreRepository.findNameById(genreId);
	}

	@Transactional(readOnly = true)
	public Map<Long, String> getGenreNamesByIds(List<Long> genreIds) {
		return genreRepository.findNamesByIds(genreIds).stream()
			.collect(Collectors.toMap(
				result -> (Long)result[0],   // Key: Genre ID
				result -> (String)result[1] // Value: Genre Name
			));
	}
}
