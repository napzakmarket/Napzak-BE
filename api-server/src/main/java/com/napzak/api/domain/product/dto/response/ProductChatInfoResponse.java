package com.napzak.api.domain.product.dto.response;

import com.napzak.common.auth.role.enums.Role;
import com.napzak.domain.product.entity.enums.TradeType;
import com.napzak.domain.product.vo.ProductWithFirstPhoto;
import com.napzak.domain.store.vo.Store;

public record ProductChatInfoResponse(
	ProductInfo productInfo,
	StoreInfo storeInfo,
	Long roomId
) {
	public static ProductChatInfoResponse from(
		ProductWithFirstPhoto product,
		Store store,
		String genreName,
		Long roomId,
		boolean isMyProduct
	) {
		boolean isWithdrawn = store.getRole().equals(Role.WITHDRAWN);
		String nickname = isWithdrawn ? "(탈퇴한 사용자) " + store.getNickname() : store.getNickname();
		return new ProductChatInfoResponse(
			new ProductInfo(
				product.getId(),
				product.getFirstPhoto(),
				product.getTradeType(),
				product.getTitle(),
				product.getPrice(),
				product.getIsPriceNegotiable(),
				genreName,
				product.getStoreId(),
				isMyProduct
			),
			new StoreInfo(
				store.getId(),
				nickname,
				store.getPhoto(),
				isWithdrawn
			),
			roomId
		);
	}

	public record ProductInfo(
		Long productId, String photo, TradeType tradeType, String title, int price,
		Boolean isPriceNegotiable, String genreName, Long productOwnerId, boolean isMyProduct) {}

	public record StoreInfo(Long storeId, String nickname, String storePhoto, boolean isWithdrawn) {}
}
