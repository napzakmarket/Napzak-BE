package com.napzak.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = ProductPhotoTableConstants.TABLE_PRODUCT_PHOTO)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPhotoEntity {

	@Id
	@Column(name = ProductPhotoTableConstants.COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = ProductPhotoTableConstants.COLUMN_PRODUCT_ID, nullable = false)
	private Long productId;

	@Column(name = ProductPhotoTableConstants.COLUMN_PHOTO_URL, nullable = false)
	private String photoUrl;

	@Column(name = ProductPhotoTableConstants.COLUMN_SEQUENCE, nullable = false)
	private int sequence;

	@Builder
	private ProductPhotoEntity(Long productId, String photoUrl, int sequence) {
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

	public void update(String photoUrl, int sequence) {
		this.photoUrl = photoUrl;
		this.sequence = sequence;
	}
}