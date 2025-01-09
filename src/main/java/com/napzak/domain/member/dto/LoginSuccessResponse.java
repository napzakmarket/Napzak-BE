package com.napzak.domain.member.dto;

import com.napzak.domain.member.core.Role;

public record LoginSuccessResponse(
        String accessToken,
        String refreshToken,
        String nickname,
        Role role
) {
    public static LoginSuccessResponse of(
            final String accessToken,
            final String refreshToken,
            final String nickname,
            final Role role
    ) {
        return new LoginSuccessResponse(accessToken, refreshToken, nickname, role);
    }
}