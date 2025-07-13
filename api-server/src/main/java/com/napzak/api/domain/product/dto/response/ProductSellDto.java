package com.napzak.api.domain.product.dto.response;

import com.napzak.domain.product.entity.enums.TradeStatus;
import com.napzak.domain.product.entity.enums.TradeType;
import com.napzak.domain.product.vo.ProductWithFirstPhoto;

public record ProductSellDto(
	Long productId,
	String genreName,
	String productName,
	String photo,
	int price,
	String uploadTime,
	boolean isInterested,
	TradeType tradeType,
	TradeStatus tradeStatus,
	boolean isOwnedByCurrentUser,
	int interestCount,
	int chatCount
) {
	// ProductWithFirstPhoto에서 genreName을 외부에서 전달받는 메서드
	public static ProductSellDto from(
		ProductWithFirstPhoto product,
		String uploadTime,
		boolean isInterested,
		String genreName,
		boolean isOwnedByCurrentUser
	) {
		return new ProductSellDto(
			product.getId(),
			genreName,
			product.getTitle(),
			product.getFirstPhoto(),
			product.getPrice(),
			uploadTime,
			isInterested,
			product.getTradeType(),
			product.getTradeStatus(),
			isOwnedByCurrentUser,
			product.getInterestCount(),
			product.getChatCount()
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
		TradeStatus tradeStatus,
		boolean isOwnedByCurrentUser,
		int interestCount,
		int chatCount
	) {
		return new ProductSellDto(
			productId, genreName, productName, photo, price, uploadTime, isInterested, tradeType, tradeStatus, isOwnedByCurrentUser, interestCount, chatCount
		);
	}
}
