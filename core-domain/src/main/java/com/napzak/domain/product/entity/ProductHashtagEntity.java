package com.napzak.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = ProductHashtagTableConstants.TABLE_PRODUCT_HASHTAG)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductHashtagEntity {

	@Id
	@Column(name = ProductHashtagTableConstants.COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = ProductHashtagTableConstants.COLUMN_PRODUCT_ID, nullable = false)
	private Long productId;

	@Column(name = ProductHashtagTableConstants.COLUMN_HASHTAG_ID, nullable = false)
	private Long hashtagId;

	@Builder
	private ProductHashtagEntity(Long productId, Long hashtagId) {
		this.productId = productId;
		this.hashtagId = hashtagId;
	}

	public static ProductHashtagEntity create(
		final Long productId,
		final Long hashtagId
	) {
		return ProductHashtagEntity.builder()
			.productId(productId)
			.hashtagId(hashtagId)
			.build();
	}
}