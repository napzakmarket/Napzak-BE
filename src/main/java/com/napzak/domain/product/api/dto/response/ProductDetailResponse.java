package com.napzak.domain.product.api.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.napzak.domain.review.api.dto.response.StoreReviewDto;
import com.napzak.domain.store.api.dto.StoreStatusDto;

public record ProductDetailResponse(
	boolean isInterested,

	@JsonProperty("productDetail")
	ProductDetailDto productDetailDto,

	@JsonProperty("productPhotoList")
	List<ProductPhotoDto> productPhotoList,

	@JsonProperty("storeInfo")
	StoreStatusDto storeInfo,

	@JsonProperty("storeReviewList")
	List<StoreReviewDto> storeReviewList
) {
}
