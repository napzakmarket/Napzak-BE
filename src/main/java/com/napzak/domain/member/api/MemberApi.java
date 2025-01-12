package com.napzak.domain.member.api;

import com.napzak.domain.member.dto.MemberLoginResponse;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.auth.client.dto.MemberLoginRequest;
import com.napzak.global.common.exception.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface MemberApi {

    ResponseEntity<SuccessResponse<MemberLoginResponse>> login(
            @RequestParam String authorizationCode,
            @RequestBody MemberLoginRequest body,
            HttpServletResponse httpServletResponse
            );

    ResponseEntity<SuccessResponse<Void>> logOut(
            @CurrentMember final Long memberId
    );
}
