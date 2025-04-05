package com.napzak.domain.store.core.vo;

import com.napzak.domain.store.core.entity.WithdrawEntity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Withdraw {
	private final Long id;
	private final String title;
	private final String description;
	private final LocalDateTime createdAt;

	public static Withdraw fromEntity(WithdrawEntity withdrawEntity) {
		return new Withdraw(
			withdrawEntity.getId(),
			withdrawEntity.getTitle(),
			withdrawEntity.getDescription(),
			withdrawEntity.getCreatedAt()
		);
	}
}
