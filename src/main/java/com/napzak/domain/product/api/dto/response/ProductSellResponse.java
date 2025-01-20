package com.napzak.domain.product.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.napzak.domain.product.core.entity.enums.ProductCondition;
import com.napzak.domain.product.core.vo.Product;
import com.napzak.domain.product.core.vo.ProductPhoto;

public record ProductSellResponse(
	long productId,
	List<ProductPhotoResponseDto> productPhotoList,
	long genreId,
	String title,
	String description,
	ProductCondition productCondition,
	int price,
	boolean isDeliveryIncluded,
	int standardDeliveryFee,
	int halfDeliveryFee,
	LocalDateTime createdAt
) {
	public static ProductSellResponse from(Product product, List<ProductPhoto> photos) {
		List<ProductPhotoResponseDto> productPhotoResponseDtos = photos.stream()
			.map(ProductPhotoResponseDto::from).toList();

		return new ProductSellResponse(
			product.getId(),
			productPhotoResponseDtos,
			product.getGenreId(),
			product.getTitle(),
			product.getDescription(),
			product.getProductCondition(),
			product.getPrice(),
			product.getIsDeliveryIncluded(),
			product.getStandardDeliveryFee(),
			product.getHalfDeliveryFee(),
			product.getCreatedAt()
		);
	}
}
