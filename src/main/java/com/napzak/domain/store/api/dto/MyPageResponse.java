package com.napzak.domain.store.api.dto;

public record MyPageResponse(
	Long storeId,
	String storeNickname,
	String storePhoto
) {
	public static MyPageResponse of(
		final Long storeId,
		final String storeNickname,
		final String storePhoto
	) {
		return new MyPageResponse(storeId, storeNickname, storePhoto);
	}
}