package com.napzak.domain.chat.entity.enums;

import com.napzak.domain.chat.entity.ChatMessageEntity;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;

public enum ChatMessageSortOption {
	OLDEST("id", com.querydsl.core.types.Order.DESC);

	private final String fieldName;
	private final com.querydsl.core.types.Order order;

	ChatMessageSortOption(String fieldName, com.querydsl.core.types.Order order) {
		this.fieldName = fieldName;
		this.order = order;
	}

	public OrderSpecifier<?> toOrderSpecifier() {
		PathBuilder<ChatMessageEntity> entityPath = new PathBuilder<>(ChatMessageEntity.class, "chatMessageEntity");
		Expression<? extends Comparable> path = entityPath.get(fieldName, Comparable.class);
		return new OrderSpecifier<>(order, path);
	}
}
