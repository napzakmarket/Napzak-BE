package com.napzak.domain.interest.core.entity;

import static com.napzak.domain.interest.core.entity.InterestTableConstants.*;

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

@Table(name = TABLE_INTEREST)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_PRODUCT_ID, nullable = false)
	private Long productId;

	@Column(name = COLUMN_STORE_ID, nullable = false)
	private Long storeId;

	@Builder
	public InterestEntity(
		Long productId,
		Long storeId) {
		this.productId = productId;
		this.storeId = storeId;
	}

	public static InterestEntity create(
		final Long productId,
		final Long storeId
	) {
		return InterestEntity.builder()
			.productId(productId)
			.storeId(storeId)
			.build();
	}
}
