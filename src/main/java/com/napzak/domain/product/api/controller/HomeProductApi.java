package com.napzak.domain.product.api.controller;

import com.napzak.domain.product.api.dto.HomeProductResponse;
import com.napzak.domain.product.api.dto.HomeBannerResponse;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.common.exception.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HomeProductApi {

    ResponseEntity<SuccessResponse<List<HomeProductResponse>>> getRecommendProducts(
            @CurrentMember final Long storeId
    );

    ResponseEntity<SuccessResponse<List<HomeProductResponse>>> getTopSellProducts(
            @CurrentMember final Long storeId
    );

    ResponseEntity<SuccessResponse<List<HomeProductResponse>>> getTopBuyProducts(
            @CurrentMember final Long storeId
    );

    ResponseEntity<SuccessResponse<List<HomeBannerResponse>>> getBanners();
}
