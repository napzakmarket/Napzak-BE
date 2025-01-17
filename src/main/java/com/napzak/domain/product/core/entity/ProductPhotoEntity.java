package com.napzak.domain.product.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.napzak.domain.product.core.entity.ProductPhotoTableConstants.*;

@Table(name = TABLE_PRODUCT_PHOTO)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPhotoEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_PRODUCT_ID, nullable = false)
	private Long productId;

	@Column(name = COLUMN_PHOTO_URL, nullable = false)
	private String photoUrl;

	@Column(name = COLUMN_SEQUENCE, nullable = false)
	private int sequence;

	@Builder
	private ProductPhotoEntity(Long id, Long productId, String photoUrl, int sequence) {
		this.id = id;
		this.productId = productId;
		this.photoUrl = photoUrl;
		this.sequence = sequence;
	}

	public static ProductPhotoEntity create(
		final Long productId,
		final String photoUrl,
		final int sequence
	) {
		return ProductPhotoEntity.builder()
			.productId(productId)
			.photoUrl(photoUrl)
			.sequence(sequence)
			.build();
	}
}