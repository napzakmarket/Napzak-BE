package com.napzak.domain.product.api.dto.response;

import com.napzak.domain.product.core.vo.Product;

public record ProductDetailDto(
	Long productId,
	String tradeType,
	String genreName,
	String productName,
	int price,
	String uploadTime,
	int viewCount,
	int interestCount,
	String description,
	String productCondition,
	int standardDeliveryFee,
	int halfDeliveryFee,
	boolean is_delivery_included,
	boolean isPriceNegotiable,
	String tradeStatus,
	boolean OwnedByCurrentUser
) {
	public static ProductDetailDto from(
		Product product,
		String genreName,
		String uploadTime,
		boolean OwnedByCurrentUser
	) {
		return new ProductDetailDto(
			product.getId(),
			product.getTradeType().toString(),
			genreName,
			product.getTitle(),
			product.getPrice(),
			uploadTime,
			product.getViewCount(),
			product.getInterestCount(),
			product.getDescription(),
			product.getProductCondition().toString(),
			product.getStandardDeliveryFee(),
			product.getHalfDeliveryFee(),
			product.getIsDeliveryIncluded(),
			product.getIsPriceNegotiable(),
			product.getTradeStatus().toString(),
			OwnedByCurrentUser
		);

	}
}
