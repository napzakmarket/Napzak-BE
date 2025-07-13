package com.napzak.api.domain.store.dto.response;

public record StoreWithdrawResponse(
	Long storeId,
	String withdrawTitle,
	String withdrawDescription
) {
	public static StoreWithdrawResponse of(
		final Long storeId,
		final String withdrawTitle,
		final String withdrawDescription
	) {
		return new StoreWithdrawResponse(storeId, withdrawTitle, withdrawDescription);
	}
}
