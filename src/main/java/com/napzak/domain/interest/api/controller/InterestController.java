package com.napzak.domain.interest.api.controller;

import com.napzak.domain.interest.api.exception.InterestErrorCode;
import com.napzak.domain.interest.api.exception.InterestSuccessCode;
import com.napzak.domain.interest.api.service.InterestService;
import com.napzak.domain.interest.core.InterestRetriever;
import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.domain.product.core.ProductRetriever;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.common.exception.NapzakException;
import com.napzak.global.common.exception.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/interest")
public class InterestController {

    private final InterestService interestService;
    private final InterestRetriever interestRetriever;
    private final ProductRetriever productRetriever;

    @PostMapping("/{productId}")
    public ResponseEntity<SuccessResponse<Void>> postInterest(
            @PathVariable("productId") Long productId,
            @CurrentMember final Long storeId
    ) {

        if (!productRetriever.existsById(productId)) { //존재하지 않는 product
            throw new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }

        if (interestRetriever.getIsInterested(productId, storeId)) {//이미 좋아요가 눌러져 있음
            throw new NapzakException(InterestErrorCode.INTEREST_ALREADY_POSTED);
        }

        interestService.postInterest(productId, storeId);

        return ResponseEntity.ok()
                .body(SuccessResponse.of(InterestSuccessCode.POST_INTEREST_SUCCESS));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<SuccessResponse<Void>> deleteInterest(
            @PathVariable("productId") Long productId,
            @CurrentMember final Long storeId
    ) {

        if (!productRetriever.existsById(productId)) { //존재하지 않는 product
            throw new NapzakException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }

        if (!interestRetriever.getIsInterested(productId, storeId)) { //좋아요가 눌려져 있지 않은 상태임
            throw new NapzakException(InterestErrorCode.INTEREST_NOT_FOUND);
        }

        interestService.deleteInterest(productId, storeId);

        return ResponseEntity.ok()
                .body(SuccessResponse.of(InterestSuccessCode.DELETE_INTEREST_SUCCESS));
    }

}
