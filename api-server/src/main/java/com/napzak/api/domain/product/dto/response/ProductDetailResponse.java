package com.napzak.api.domain.product.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.napzak.domain.store.vo.StoreStatus;

public record ProductDetailResponse(
	boolean isInterested,

	@JsonProperty("productDetail")
	ProductDetailDto productDetailDto,

	@JsonProperty("productPhotoList")
	List<ProductPhotoDto> productPhotoList,

	@JsonProperty("storeInfo")
	StoreStatus storeInfo
) {
}
