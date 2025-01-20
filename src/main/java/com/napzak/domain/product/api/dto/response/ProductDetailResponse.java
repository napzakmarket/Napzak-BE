package com.napzak.domain.product.api.dto.response;

import java.util.List;

import com.napzak.domain.review.api.dto.response.StoreReviewDto;
import com.napzak.domain.store.api.dto.StoreStatusDto;

public record ProductDetailResponse(
	boolean isInterested,
	ProductDetailDto productDetailDto,
	List<ProductPhotoDto> productPhotoList,
	StoreStatusDto storeInfo,
	List<StoreReviewDto> storeReviewList
) {
}
