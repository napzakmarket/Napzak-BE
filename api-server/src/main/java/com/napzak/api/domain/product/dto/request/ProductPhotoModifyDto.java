package com.napzak.api.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductPhotoModifyDto(
	Long photoId,

	@NotBlank(message = "사진 URL은 비어 있을 수 없습니다.")
	String photoUrl,

	@Positive(message = "사진 순서는 1 이상이어야 합니다.")
	int sequence
) implements ProductPhotoBase {
	@Override public int sequence() { return sequence;}
}
