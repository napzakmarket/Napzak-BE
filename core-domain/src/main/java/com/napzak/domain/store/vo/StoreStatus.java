package com.napzak.domain.store.vo;

public record StoreStatus(
	Long userId,
	String storePhoto,
	String nickname,
	Long totalSellCount,
	Long totalBuyCount
) {
	public static StoreStatus from(
		Long userId,
		String storePhoto,
		String nickname,
		Long totalSellCount,
		Long totalBuyCount
	) {
		return new StoreStatus(userId, storePhoto, nickname, totalSellCount, totalBuyCount);
	}
}
