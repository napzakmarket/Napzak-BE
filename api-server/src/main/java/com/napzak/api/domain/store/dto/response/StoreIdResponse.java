package com.napzak.api.domain.store.dto.response;

public record StoreIdResponse(
	Long storeId
) {
	public static StoreIdResponse of(Long storeId) {
		return new StoreIdResponse(storeId);
	}
}
