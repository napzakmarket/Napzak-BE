package com.napzak.api.domain.store.dto.response;

public record MyPageResponse(
	Long storeId,
	String storeNickName,
	String storePhoto,
	int totalSellCount,
	int totalBuyCount,
	String serviceLink
) {
	public static MyPageResponse of(
		final Long storeId,
		final String storeNickName,
		final String storePhoto,
		final int totalSellCount,
		final int totalBuyCount,
		final String serviceLink
	) {
		return new MyPageResponse(storeId, storeNickName, storePhoto, totalSellCount, totalBuyCount, serviceLink);
	}
}