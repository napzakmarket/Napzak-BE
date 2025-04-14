package com.napzak.domain.store.api.dto.response;

public record StoreStatusDto(
	Long userId,
	String storePhoto,
	String nickname,
	Long totalSellCount,
	Long totalBuyCount
) {
	public static StoreStatusDto from(
		Long userId,
		String storePhoto,
		String nickname,
		Long totalSellCount,
		Long totalBuyCount
	) {
		return new StoreStatusDto(userId, storePhoto, nickname, totalSellCount, totalBuyCount);
	}
}
