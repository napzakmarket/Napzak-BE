package com.napzak.domain.product.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.napzak.domain.product.core.entity.ProductHashtagTableConstants.*;

@Entity
@Table(name = TABLE_PRODUCT_HASHTAG)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductHashtagEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_PRODUCT_ID, nullable = false)
	private Long productId;

	@Column(name = COLUMN_HASHTAG_ID, nullable = false)
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