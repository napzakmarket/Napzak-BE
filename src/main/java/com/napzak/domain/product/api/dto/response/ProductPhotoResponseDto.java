package com.napzak.domain.product.api.dto.response;

import com.napzak.domain.product.core.vo.ProductPhoto;

public record ProductPhotoResponseDto(
	long photoId,
	String photoUrl,
	int sequence
) {
	public static ProductPhotoResponseDto from(ProductPhoto photo) {
		return new ProductPhotoResponseDto(photo.getId(),
			photo.getPhotoUrl(),
			photo.getSequence());
	}
}
