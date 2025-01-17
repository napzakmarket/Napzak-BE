package com.napzak.domain.product.api.controller;

import com.napzak.domain.product.api.ProductStoreFacade;
import com.napzak.domain.product.api.dto.HomeProductResponse;
import com.napzak.domain.product.api.exception.ProductSuccessCode;
import com.napzak.domain.product.api.service.HomeProductService;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.common.exception.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/home")
@RequiredArgsConstructor
public class HomeProductController implements HomeProductApi{

    private final HomeProductService homeProductService;
    private final ProductStoreFacade productStoreFacade;

    @GetMapping("/recommend")
    @Override
    public ResponseEntity<SuccessResponse<List<HomeProductResponse>>> getRecommendProducts(
            @CurrentMember final Long storeId
    ){

        if(productStoreFacade.existGenrePreference(storeId)){

            List<HomeProductResponse> homeProductResponses = homeProductService.getRecommendedProducts(storeId);
            return ResponseEntity.ok()
                    .body(SuccessResponse.of(ProductSuccessCode.RECOMMEND_PRODUCT_GET_SUCCESS, homeProductResponses));
        } else{

            List<HomeProductResponse> homeProductResponses = homeProductService.getSpecificProducts(storeId);
            return ResponseEntity.ok()
                    .body(SuccessResponse.of(ProductSuccessCode.RECOMMEND_PRODUCT_GET_SUCCESS, homeProductResponses));
        }
    }

    @GetMapping("/sell")
    @Override
    public ResponseEntity<SuccessResponse<List<HomeProductResponse>>> getTopSellProducts(
            @CurrentMember final Long storeId
    ){

        List<HomeProductResponse> homeProductResponses = homeProductService.getTopSellProducts(storeId);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(ProductSuccessCode.TOP_SELL_PRODUCT_GET_SUCCESS, homeProductResponses));
    }

    @GetMapping("/buy")
    @Override
    public ResponseEntity<SuccessResponse<List<HomeProductResponse>>> getTopBuyProducts(
            @CurrentMember final Long storeId
    ){

        List<HomeProductResponse> homeProductResponses = homeProductService.getTopBuyProducts(storeId);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(ProductSuccessCode.TOP_BUY_PRODUCT_GET_SUCCESS, homeProductResponses));
    }



}
