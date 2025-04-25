package com.napzak.domain.store.api.dto.request;

import java.util.List;

import javax.annotation.Nullable;

import jakarta.validation.constraints.Size;

public record StoreProfileModifyRequest(
	String storeCover,
	String storePhoto,
	@Size(max = 20)
	String storeNickName,
	@Nullable
	@Size(max = 200)
	String storeDescription,
	List<Long> preferredGenreList
) {
}
