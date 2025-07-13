package com.napzak.domain.genre.entity.enums;

import com.napzak.domain.product.entity.ProductEntity;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;

public enum GenreSortOption {
	OLDEST("id", com.querydsl.core.types.Order.ASC),
	;

	private final String fieldName;
	private final com.querydsl.core.types.Order order;

	GenreSortOption(String fieldName, com.querydsl.core.types.Order order) {
		this.fieldName = fieldName;
		this.order = order;
	}

	public String getFieldName() {
		return fieldName;
	}

	public com.querydsl.core.types.Order getOrder() {
		return order;
	}

	public OrderSpecifier<?> toOrderSpecifier() {
		PathBuilder<ProductEntity> entityPath = new PathBuilder<>(ProductEntity.class, "genreEntity");
		Expression<? extends Comparable> path = entityPath.get(fieldName, Comparable.class);

		return new OrderSpecifier<>(order, path);
	}
}
