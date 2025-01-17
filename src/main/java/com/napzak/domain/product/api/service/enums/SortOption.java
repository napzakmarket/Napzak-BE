package com.napzak.domain.product.api.service.enums;

import com.napzak.domain.product.core.entity.ProductEntity;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;

public enum SortOption {
	RECENT("id", com.querydsl.core.types.Order.DESC),
	POPULAR("interestCount", com.querydsl.core.types.Order.DESC),
	LOW_PRICE("price", com.querydsl.core.types.Order.ASC),
	HIGH_PRICE("price", com.querydsl.core.types.Order.DESC);

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
		PathBuilder<ProductEntity> entityPath = new PathBuilder<>(ProductEntity.class, "productEntity");
		Expression<? extends Comparable> path = entityPath.get(fieldName, Comparable.class);

		return new OrderSpecifier<>(order, path);
	}
}
