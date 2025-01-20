package com.napzak.domain.product.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductPhotoRequestDto(
	@NotBlank(message = "사진 URL은 비어 있을 수 없습니다.")
	String photoUrl,

	@Positive(message = "사진 순서는 1 이상이어야 합니다.")
	int sequence
) {
}
