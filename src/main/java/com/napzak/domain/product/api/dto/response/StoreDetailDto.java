package com.napzak.domain.product.api.dto.response;

public record StoreDetailDto(
	Long userId,
	String storePhoto,
	String nickname,
	int totalProducts,
	int totalTransactions
) {
}
