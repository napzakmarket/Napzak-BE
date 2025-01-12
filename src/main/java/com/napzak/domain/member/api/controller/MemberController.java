package com.napzak.domain.member.api.controller;

import com.napzak.domain.member.api.service.LoginService;
import com.napzak.domain.member.api.dto.LoginSuccessResponse;
import com.napzak.domain.member.api.dto.MemberLoginResponse;
import com.napzak.domain.member.core.exception.MemberSuccessCode;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.auth.client.dto.MemberLoginRequest;
import com.napzak.global.auth.jwt.service.TokenService;
import com.napzak.global.common.exception.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final LoginService loginService;
    private final TokenService tokenService;

    private static final String REFRESH_TOKEN = "refreshToken";
    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60;

    @PostMapping("/login")
    @Override
    public ResponseEntity<SuccessResponse<MemberLoginResponse>> login(
            String authorizationCode,
            MemberLoginRequest memberLoginRequest,
            HttpServletResponse httpServletResponse
    ){
            LoginSuccessResponse successResponse = loginService.login(authorizationCode, memberLoginRequest);
            ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, successResponse.refreshToken())
                    .maxAge(COOKIE_MAX_AGE)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .httpOnly(true)
                    .build();
            httpServletResponse.setHeader("Set-Cookie", cookie.toString());

            MemberLoginResponse response = MemberLoginResponse.of(
                    successResponse.accessToken(),
                    successResponse.nickname(),
                    successResponse.role());

            return ResponseEntity.ok()
                    .body(SuccessResponse.of(MemberSuccessCode.LOGIN_SUCCESS, response));
        }

    @PostMapping("/logout")
    @Override
    public ResponseEntity<SuccessResponse<Void>> logOut(
            @CurrentMember final Long memberId
    ){
        tokenService.deleteRefreshToken(memberId);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(MemberSuccessCode.LOGOUT_SUCCESS, null));
    }
}
