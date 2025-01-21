package com.napzak.domain.store.api.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StoreNormalRegisterRequest(
	List<Long> genrePreferenceList,

	@NotBlank(message = "닉네임은 필수 입력 사항입니다.")
	String nickname,

	@NotBlank(message = "전화번호는 필수 입력 사항입니다.")
	String phoneNumber,

	@Size(max = 58, message = "최대 58자 제한입니다.")
	@NotBlank(message = "상점 소개는 필수 입력 사항입니다.")
	String description,

	String photo
) {

}
