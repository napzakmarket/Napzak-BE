package com.napzak.domain.product.api.dto.response;

import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.ProductWithFirstPhoto;

public record ProductChatResponse(
	String nickname,
	String firstPhoto,
	TradeType tradeType,
	String title,
	int price,
	Boolean isPriceNegotiable
) {
	public static ProductChatResponse from(
		ProductWithFirstPhoto product,
		String nickname
	) {
		return new ProductChatResponse(
			nickname,
			product.getFirstPhoto(),
			product.getTradeType(),
			product.getTitle(),
			product.getPrice(),
			product.getIsPriceNegotiable()
		);
	}
}
