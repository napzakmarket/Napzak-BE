package com.napzak.domain.product.core.vo;

import com.napzak.domain.product.core.entity.ProductPhotoEntity;

import lombok.Getter;

@Getter
public class ProductPhoto {
	private final Long id;
	private final Long productId;
	private final String photoUrl;
	private final int sequence;

	public ProductPhoto(Long id, Long productId, String photoUrl, int sequence) {
		this.id = id;
		this.productId = productId;
		this.photoUrl = photoUrl;
		this.sequence = sequence;
	}

	public static ProductPhoto fromEntity(ProductPhotoEntity productPhotoEntity) {
		return new ProductPhoto(
			productPhotoEntity.getId(),
			productPhotoEntity.getProductId(),
			productPhotoEntity.getPhotoUrl(),
			productPhotoEntity.getSequence()
		);
	}
}