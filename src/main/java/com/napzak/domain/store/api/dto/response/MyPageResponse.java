package com.napzak.domain.store.api.dto.response;

public record MyPageResponse(
	Long storeId,
	String storeNickName,
	String storePhoto
) {
	public static MyPageResponse of(
		final Long storeId,
		final String storeNickName,
		final String storePhoto
	) {
		return new MyPageResponse(storeId, storeNickName, storePhoto);
	}
}