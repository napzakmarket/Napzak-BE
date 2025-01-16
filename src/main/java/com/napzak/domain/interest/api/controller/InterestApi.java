package com.napzak.domain.interest.api.controller;

import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.common.exception.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface InterestApi {

    ResponseEntity<SuccessResponse<Void>> postInterest(
            @RequestParam Long productId,
            @CurrentMember final Long storeId
    );
}
