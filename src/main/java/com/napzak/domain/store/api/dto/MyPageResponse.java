package com.napzak.domain.store.api.dto;

public record MyPageResponse(
	Long storeId,
	String storeNickName,
	String storePhoto,
	String storeCover
) {
	public static MyPageResponse of(
		final Long storeId,
		final String storeNickName,
		final String storePhoto,
		final String storeCover
	) {
		return new MyPageResponse(storeId, storeNickName, storePhoto, storeCover);
	}
}