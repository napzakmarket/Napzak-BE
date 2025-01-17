package com.napzak.domain.product.api.dto.response;

import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.ProductWithFirstPhoto;

public record ProductSellDto(
	Long productId,
	String genreName,
	String productName,
	String photo,
	int price,
	String uploadTime,
	boolean isInterested,
	TradeType tradeType,
	TradeStatus tradeStatus
) {
	// ProductWithFirstPhoto에서 genreName을 외부에서 전달받는 메서드
	public static ProductSellDto from(
		ProductWithFirstPhoto product,
		String firstPhoto,
		String uploadTime,
		boolean isInterested,
		String genreName
	) {
		return new ProductSellDto(
			product.getId(),
			genreName, // 외부에서 전달받은 genreName
			product.getTitle(),
			firstPhoto,
			product.getPrice(),
			uploadTime,
			isInterested,
			product.getTradeType(),
			product.getTradeStatus()
		);
	}

	// 별도의 매핑 로직이 필요한 경우를 위한 오버로드된 메서드
	public static ProductSellDto from(
		Long productId,
		String genreName,
		String productName,
		String photo,
		int price,
		String uploadTime,
		boolean isInterested,
		TradeType tradeType,
		TradeStatus tradeStatus
	) {
		return new ProductSellDto(
			productId, genreName, productName, photo, price, uploadTime, isInterested, tradeType, tradeStatus
		);
	}
}
