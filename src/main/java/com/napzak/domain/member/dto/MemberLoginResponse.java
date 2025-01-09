package com.napzak.domain.member.dto;

import com.napzak.domain.member.core.Role;

public record MemberLoginResponse(
        String accessToken,
        String nickname,
        Role role
) {
    public static MemberLoginResponse of(
            final String accessToken,
            final String nickname,
            final Role role
    ) {
        return new MemberLoginResponse(accessToken, nickname, role);
    }
}
