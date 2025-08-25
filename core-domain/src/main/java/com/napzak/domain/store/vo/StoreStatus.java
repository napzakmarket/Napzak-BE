package com.napzak.domain.store.vo;

public record StoreStatus(
	Long userId,
	String storePhoto,
	String nickname,
	int totalSellCount,
	int totalBuyCount
) {
	public static StoreStatus from(
		Long userId,
		String storePhoto,
		String nickname,
		int totalSellCount,
		int totalBuyCount
	) {
		return new StoreStatus(userId, storePhoto, nickname, totalSellCount, totalBuyCount);
	}
}
