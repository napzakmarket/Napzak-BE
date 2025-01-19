package com.napzak.domain.genre.api.service.enums;

import com.napzak.domain.product.core.entity.ProductEntity;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;

public enum SortOption {
	OLDEST("id", com.querydsl.core.types.Order.ASC),
	;

	private final String fieldName;
	private final com.querydsl.core.types.Order order;

	SortOption(String fieldName, com.querydsl.core.types.Order order) {
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
