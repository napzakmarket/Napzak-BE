package com.napzak.domain.store.api.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record StoreNormalRegisterRequest(
	List<Long> genrePreferenceList,

	@NotNull(message = "닉네임은 필수 입력 사항입니다.")
	String nickname,

	@NotNull(message = "전화번호는 필수 입력 사항입니다.")
	String phoneNumber,

	@NotNull(message = "상점 소개는 필수 입력 사항입니다.")
	String description,

	String photo
) {

}
