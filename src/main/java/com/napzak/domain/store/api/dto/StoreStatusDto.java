package com.napzak.domain.store.api.dto;

public record StoreStatusDto(
	Long userId,
	String storePhoto,
	String nickname,
	Long totalProducts,
	Long totalTransactions
) {
	public static StoreStatusDto from(
		Long userId,
		String storePhoto,
		String nickname,
		Long totalProducts,
		Long totalTransactions
	) {
		return new StoreStatusDto(userId, storePhoto, nickname, totalProducts, totalTransactions);
	}
}
