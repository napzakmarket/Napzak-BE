package com.napzak.domain.interest.api.controller;

import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.common.exception.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface InterestApi {

    ResponseEntity<SuccessResponse<Void>> postInterest(
            @PathVariable("productId") Long productId,
            @CurrentMember final Long storeId
    );

    ResponseEntity<SuccessResponse<Void>> deleteInterest(
            @PathVariable("productId") Long productId,
            @CurrentMember final Long storeId
    );
}
