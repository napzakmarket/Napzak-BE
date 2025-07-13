package com.napzak.domain.product.vo;

import com.napzak.domain.product.entity.ProductHashtagEntity;

import lombok.Getter;

@Getter
public class ProductHashtag {
	private final Long id;
	private final Long productId;
	private final Long hashtagId;

	public ProductHashtag(Long id, Long productId, Long hashtagId) {
		this.id = id;
		this.productId = productId;
		this.hashtagId = hashtagId;
	}

	public static ProductHashtag fromEntity(ProductHashtagEntity productHashtagEntity) {
		return new ProductHashtag(
			productHashtagEntity.getId(),
			productHashtagEntity.getProductId(),
			productHashtagEntity.getHashtagId()
		);
	}
}