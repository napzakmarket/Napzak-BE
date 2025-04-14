package com.napzak.domain.product.api.dto.response;

import com.napzak.domain.product.core.entity.enums.TradeStatus;
import com.napzak.domain.product.core.entity.enums.TradeType;
import com.napzak.domain.product.core.vo.ProductWithFirstPhoto;

public record ProductBuyDto(
	Long productId,
	String genreName,
	String productName,
	String photo,
	int price,
	String uploadTime,
	boolean isInterested,
	TradeType tradeType,
	TradeStatus tradeStatus,
	boolean isPriceNegotiable,
	boolean isOwnedByCurrentUser,
	int interestCount,
	int chatCount
) {
	// ProductWithFirstPhoto에서 genreName을 외부에서 전달받는 메서드
	public static ProductBuyDto from(
		ProductWithFirstPhoto product,
		String uploadTime,
		boolean isInterested,
		String genreName,
		boolean isOwnedByCurrentUser
	) {
		return new ProductBuyDto(
			product.getId(),
			genreName,
			product.getTitle(),
			product.getFirstPhoto(),
			product.getPrice(),
			uploadTime,
			isInterested,
			product.getTradeType(),
			product.getTradeStatus(),
			product.getIsPriceNegotiable(),
			isOwnedByCurrentUser,
			product.getInterestCount(),
			product.getChatCount()
		);
	}

	// 별도의 매핑 로직이 필요한 경우를 위한 오버로드된 메서드
	public static ProductBuyDto from(
		Long productId,
		String genreName,
		String productName,
		String photo,
		int price,
		String uploadTime,
		boolean isInterested,
		TradeType tradeType,
		TradeStatus tradeStatus,
		boolean isPriceNegotiable,
		boolean isOwnedByCurrentUser,
		int interestCount,
		int chatCount
	) {
		return new ProductBuyDto(
			productId, genreName, productName, photo, price, uploadTime, isInterested,
			tradeType, tradeStatus, isPriceNegotiable, isOwnedByCurrentUser, interestCount, chatCount
		);
	}
}
