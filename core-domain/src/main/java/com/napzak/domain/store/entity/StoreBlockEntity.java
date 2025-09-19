package com.napzak.domain.store.entity;

import static com.napzak.domain.store.entity.StoreBlockTableConstants.*;

import com.napzak.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = TABLE_STORE_BLOCK)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreBlockEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_BLOCKER_STORE_ID, nullable = false)
	private Long blockerStoreId;

	@Column(name = COLUMN_BLOCKED_STORE_ID, nullable=false)
	private Long blockedStoreId;

	@Builder
	public StoreBlockEntity(Long blockerStoreId, Long blockedStoreId) {
		this.blockerStoreId = blockerStoreId;
		this.blockedStoreId = blockedStoreId;
	}

	public static StoreBlockEntity create(Long blockerStoreId, Long blockedStoreId) {
		return StoreBlockEntity.builder()
			.blockerStoreId(blockerStoreId)
			.blockedStoreId(blockedStoreId)
			.build();
	}
}
