package com.napzak.domain.product.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.napzak.domain.product.core.vo.Product;
import com.napzak.domain.product.core.vo.ProductPhoto;

public record ProductBuyResponse(
	long productId,
	List<ProductPhotoResponseDto> productPhotoList,
	long genreId,
	String title,
	String description,
	int price,
	boolean isPriceNegotiable,
	LocalDateTime createdAt
) {
	public static ProductBuyResponse from(Product product, List<ProductPhoto> photos) {
		List<ProductPhotoResponseDto> productPhotoResponseDtos = photos.stream()
			.map(ProductPhotoResponseDto::from).toList();

		return new ProductBuyResponse(
			product.getId(),
			productPhotoResponseDtos,
			product.getGenreId(),
			product.getTitle(),
			product.getDescription(),
			product.getPrice(),
			product.getIsPriceNegotiable(),
			product.getCreatedAt()
		);
	}
}
