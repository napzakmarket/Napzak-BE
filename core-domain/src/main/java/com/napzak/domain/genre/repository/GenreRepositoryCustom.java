package com.napzak.domain.genre.repository;

import java.util.List;

import com.napzak.domain.genre.entity.GenreEntity;
import com.querydsl.core.types.OrderSpecifier;

public interface GenreRepositoryCustom {
	List<GenreEntity> getGenresByWithCursor(
		OrderSpecifier<?> orderSpecifier, Long cursorGenreId, int size);

	List<GenreEntity> searchGenresBySearchWordWithCursor(
		String searchWord, OrderSpecifier<?> orderSpecifier, Long cursorGenreId, int size);
}
