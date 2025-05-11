package com.napzak.domain.product.api.dto.response;

import com.napzak.domain.product.core.vo.ProductPhoto;

public record ProductPhotoDto(
	Long photoId,
	String photoUrl,
	int sequence
) {
	public static ProductPhotoDto from(ProductPhoto photo) {
		return new ProductPhotoDto(
			photo.getId(),
			photo.getPhotoUrl(),
			photo.getSequence()
		);
	}
}