package com.napzak.domain.product.api.dto.response;

import java.util.List;

import com.napzak.domain.product.core.entity.enums.ProductCondition;

public record ProductSellModifyResponse(
	Long productId,
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
		return new ProductSellModifyResponse(productId, genreName, title, description, productCondition, price,
			isDeliveryIncluded, standardDeliveryFee, halfDeliveryFee, productPhotoList);
	}
}
