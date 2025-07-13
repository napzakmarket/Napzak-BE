package com.napzak.api.domain.product.dto.response;

import com.napzak.domain.product.vo.ProductPhoto;

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
