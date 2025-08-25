package com.napzak.api.domain.product.dto.response;

import java.util.List;

import com.napzak.domain.product.entity.enums.ProductCondition;

public record ProductSellModifyResponse(
	Long productId,
	Long genreId,
	String genreName,
	String title,
	String description,
	ProductCondition productCondition,
	int price,
	boolean isDeliveryIncluded,
	int standardDeliveryFee,
	int halfDeliveryFee,
	List<ProductPhotoDto> productPhotoList
) {
	public static ProductSellModifyResponse from(
		Long productId,
		Long genreId,
		String genreName,
		String title,
		String description,
		ProductCondition productCondition,
		int price,
		boolean isDeliveryIncluded,
		int standardDeliveryFee,
		int halfDeliveryFee,
		List<ProductPhotoDto> productPhotoList
	) {
		return new ProductSellModifyResponse(productId, genreId, genreName, title, description, productCondition, price,
			isDeliveryIncluded, standardDeliveryFee, halfDeliveryFee, productPhotoList);
	}
}
