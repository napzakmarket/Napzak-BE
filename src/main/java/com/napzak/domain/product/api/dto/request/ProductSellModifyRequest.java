package com.napzak.domain.product.api.dto.request;

import java.util.List;

import com.napzak.domain.product.api.annotation.ValidSequence;
import com.napzak.domain.product.core.entity.enums.ProductCondition;
import com.napzak.global.common.annotation.ValidEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductSellModifyRequest(
	@NotNull
	@Size(min = 1, max =10, message = "사진은 최소 1개 이상, 최대 10개까지 등록할 수 있습니다.")
	@Valid
	@ValidSequence
	List<ProductPhotoModifyDto> productPhotoList,

	@NotNull
	long genreId,

	@NotBlank(message = "상품 제목을 입력해 주세요.")
	@Size(max = 50, message = "상품 제목은 50자를 초과할 수 없습니다.")
	String title,

	@NotBlank(message = "상품 설명을 입력해 주세요.")
	@Size(max = 450, message = "상품 설명은 450자를 초과할 수 없습니다.")
	String description,

	@NotNull
	@ValidEnum(enumClass = ProductCondition.class, message = "유효하지 않은 상품 상태입니다.")
	ProductCondition productCondition,

	@NotNull
	@Positive(message = "가격은 0보다 커야합니다.")
	int price,

	boolean isDeliveryIncluded,

	@NotNull
	@PositiveOrZero(message = "일반배송비는 0 이상이어야 합니다.")
	int standardDeliveryFee,

	@NotNull
	@PositiveOrZero(message = "반값배송비는 0 이상이어야 합니다.")
	int halfDeliveryFee
) {
}
