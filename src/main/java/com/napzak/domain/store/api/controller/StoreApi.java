 package com.napzak.domain.store.api.controller;

import com.napzak.domain.store.api.dto.MyPageResponse;
import com.napzak.domain.store.api.dto.StoreInfoResponse;
import com.napzak.domain.store.api.dto.StoreLoginResponse;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.auth.client.dto.StoreSocialLoginRequest;
import com.napzak.global.common.exception.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface StoreApi {

    ResponseEntity<SuccessResponse<StoreLoginResponse>> login(
            @RequestParam String authorizationCode,
            @RequestBody StoreSocialLoginRequest storeSocialLoginRequest,
            HttpServletResponse httpServletResponse
    );

    ResponseEntity<SuccessResponse<Void>> logOut(
            @CurrentMember final Long storeId
    );

    ResponseEntity<SuccessResponse<MyPageResponse>> getMyPageInfo(
            @CurrentMember final Long storeId
    );

    ResponseEntity<SuccessResponse<StoreInfoResponse>> getStoreInfo(
            @PathVariable("storeId") Long storeId
    );
}
