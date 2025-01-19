package com.napzak.domain.product.api.dto.response;

public record ProductPhotoDto(
	Long photoId,
	String photoUrl,
	int photoSequence) {
	
}