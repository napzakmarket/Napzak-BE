package com.napzak.domain.product.api.controller;

import com.napzak.domain.product.api.ProductStoreFacade;
import com.napzak.domain.product.api.dto.HomeProductResponse;
import com.napzak.domain.product.api.exception.ProductSuccessCode;
import com.napzak.domain.product.api.service.HomeProductService;
import com.napzak.domain.product.api.service.ProductService;
import com.napzak.domain.product.core.ProductRetriever;
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

        if(productStoreFacade.findGenrePreference(storeId)){

            List<HomeProductResponse> homeProductResponses = homeProductService.getRecommendedProducts(storeId);
            return ResponseEntity.ok()
                    .body(SuccessResponse.of(ProductSuccessCode.PRODUCT_GET_SUCCESS, homeProductResponses));
        } else{

            List<HomeProductResponse> homeProductResponses = homeProductService.getSpecificProducts(storeId);
            return ResponseEntity.ok()
                    .body(SuccessResponse.of(ProductSuccessCode.PRODUCT_GET_SUCCESS, homeProductResponses));
        }
    }


}
