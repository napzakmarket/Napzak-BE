package com.napzak.domain.genre.core;

import static com.napzak.domain.genre.core.entity.QGenreEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.napzak.domain.genre.core.entity.GenreEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<GenreEntity> getGenresByWithCursor(
		OrderSpecifier<?> orderSpecifier, Long cursorGenreId, int size) {
		return queryFactory.selectFrom(genreEntity)
			.where(cursorFilter(cursorGenreId, orderSpecifier))
			.orderBy(orderSpecifier)
			.limit(size + 1)
			.fetch();
	}

	@Override
	public List<GenreEntity> searchGenresBySearchWordWithCursor(
		String searchWord, OrderSpecifier<?> orderSpecifier, Long cursorGenreId, int size) {
		return queryFactory.selectFrom(genreEntity)
			.where(
				matchGenreName(searchWord).gt(0),
				cursorFilter(cursorGenreId, orderSpecifier)
			)
			.orderBy(orderSpecifier)
			.limit(size + 1)
			.fetch();
	}

	private BooleanExpression cursorFilter(Long cursorGenreId, OrderSpecifier<?> orderSpecifier) {
		if (cursorGenreId == null) {
			return null;
		}

		String fieldName = ((PathBuilder<?>) orderSpecifier.getTarget()).getMetadata().getName();
		com.querydsl.core.types.Order direction = orderSpecifier.getOrder();

		if (fieldName.equals("id")) {
			return direction == com.querydsl.core.types.Order.ASC
				? genreEntity.id.goe(cursorGenreId)
				: genreEntity.id.loe(cursorGenreId);
		}

		return null;
	}

	private NumberTemplate<Double> matchGenreName(String searchWord) {
		if (searchWord == null || searchWord.isBlank()) {
			return Expressions.numberTemplate(Double.class, "0");
		}

		String[] tokens = searchWord.trim().split("\\s+");
		String booleanQuery = "+" + String.join(" +", tokens);

		log.info("Generated Boolean Query for searchWord '{}': {}", searchWord, booleanQuery);

		return Expressions.numberTemplate(
			Double.class,
			"function('match', {0}, {1})",
			genreEntity.name,
			booleanQuery
		);
	}

}
