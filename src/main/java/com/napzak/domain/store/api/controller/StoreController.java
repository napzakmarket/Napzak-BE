package com.napzak.domain.store.api.controller;

import com.napzak.domain.store.api.dto.MyPageResponse;
import com.napzak.domain.store.api.dto.StoreInfoResponse;
import com.napzak.domain.store.api.exception.StoreErrorCode;
import com.napzak.domain.store.api.service.LoginService;
import com.napzak.domain.store.api.dto.LoginSuccessResponse;
import com.napzak.domain.store.api.dto.StoreLoginResponse;
import com.napzak.domain.store.api.exception.StoreSuccessCode;
import com.napzak.domain.store.api.service.StoreService;
import com.napzak.domain.store.core.StoreRetriever;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.auth.client.dto.StoreSocialLoginRequest;
import com.napzak.global.auth.jwt.service.TokenService;
import com.napzak.global.common.exception.NapzakException;
import com.napzak.global.common.exception.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("api/v1/store")
@RequiredArgsConstructor
public class StoreController implements StoreApi {

    private final LoginService loginService;
    private final TokenService tokenService;
    private final StoreService storeService;
    private final StoreRetriever storeRetriever;

    private static final String REFRESH_TOKEN = "refreshToken";
    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60;

    @PostMapping("/login")
    @Override
    public ResponseEntity<SuccessResponse<StoreLoginResponse>> login(
            String authorizationCode,
            StoreSocialLoginRequest storeSocialLoginRequest,
            HttpServletResponse httpServletResponse
    ){
            LoginSuccessResponse successResponse = loginService.login(authorizationCode, storeSocialLoginRequest);
            ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, successResponse.refreshToken())
                    .maxAge(COOKIE_MAX_AGE)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .httpOnly(true)
                    .build();
            httpServletResponse.setHeader("Set-Cookie", cookie.toString());

            StoreLoginResponse response = StoreLoginResponse.of(
                    successResponse.accessToken(),
                    successResponse.nickname(),
                    successResponse.role());

            return ResponseEntity.ok()
                    .body(SuccessResponse.of(StoreSuccessCode.LOGIN_SUCCESS, response));
        }

    @PostMapping("/logout")
    @Override
    public ResponseEntity<SuccessResponse<Void>> logOut(
            @CurrentMember final Long storeId
    ){
        tokenService.deleteRefreshToken(storeId);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(StoreSuccessCode.LOGOUT_SUCCESS, null));
    }

    @GetMapping("/mypage")
    @Override
    public ResponseEntity<SuccessResponse<MyPageResponse>> getMyPageInfo(
            @CurrentMember final Long storeId
    ) {
        MyPageResponse myPageResponse = storeService.getMyPageInfo(storeId);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(StoreSuccessCode.GET_MYPAGE_INFO_SUCCESS, myPageResponse));
    }

    @GetMapping("/{storeId}")
    @Override
    public ResponseEntity<SuccessResponse<StoreInfoResponse>> getStoreInfo(
            @PathVariable("storeId") Long storeId
    ) {
        if (!storeRetriever.existsById(storeId)) {
            throw new NapzakException(StoreErrorCode.STORE_NOT_FOUND);
        }
        StoreInfoResponse storeInfoResponse = storeService.getStoreInfo(storeId);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(StoreSuccessCode.GET_STORE_INFO_SUCCESS, storeInfoResponse));
    }
}
