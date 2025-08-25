package com.napzak.api.domain.product.dto.response;

import com.napzak.domain.product.vo.ProductPhoto;

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