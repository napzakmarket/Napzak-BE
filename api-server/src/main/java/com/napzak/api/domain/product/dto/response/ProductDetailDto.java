package com.napzak.api.domain.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.napzak.domain.product.vo.Product;

public record ProductDetailDto(
	Long productId,
	String tradeType,
	String genreName,
	String productName,
	int price,
	String uploadTime,
	int chatCount,
	int interestCount,
	String description,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	String productCondition,
	int standardDeliveryFee,
	int halfDeliveryFee,
	boolean isDeliveryIncluded,
	boolean isPriceNegotiable,
	String tradeStatus,
	boolean isOwnedByCurrentUser
) {
	public static ProductDetailDto from(
		Product product,
		String uploadTime,
		String genreName,
		boolean isOwnedByCurrentUser
	) {
		return new ProductDetailDto(
			product.getId(),
			product.getTradeType().toString(),
			genreName,
			product.getTitle(),
			product.getPrice(),
			uploadTime,
			product.getChatCount(),
			product.getInterestCount(),
			product.getDescription(),
			product.getProductCondition() != null ? product.getProductCondition().toString() : null,
			product.getStandardDeliveryFee(),
			product.getHalfDeliveryFee(),
			product.getIsDeliveryIncluded(),
			product.getIsPriceNegotiable(),
			product.getTradeStatus().toString(),
			isOwnedByCurrentUser
		);

	}
}
