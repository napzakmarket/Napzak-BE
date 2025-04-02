package com.napzak.domain.store.core.vo;

import com.napzak.domain.store.core.entity.WithdrawnStoreEntity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WithdrawnStore {
	private final Long id;
	private final String title;
	private final String description;
	private final LocalDateTime createdAt;

	public static WithdrawnStore fromEntity(WithdrawnStoreEntity withdrawnStoreEntity) {
		return new WithdrawnStore(
			withdrawnStoreEntity.getId(),
			withdrawnStoreEntity.getTitle(),
			withdrawnStoreEntity.getDescription(),
			withdrawnStoreEntity.getCreatedAt()
		);
	}
}
