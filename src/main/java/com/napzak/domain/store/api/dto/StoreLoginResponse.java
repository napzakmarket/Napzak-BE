package com.napzak.domain.store.api.dto;

import com.napzak.domain.store.core.entity.enums.Role;

public record StoreLoginResponse(
        String accessToken,
        String nickname,
        Role role
) {
    public static StoreLoginResponse of(
            final String accessToken,
            String nickname,
            final Role role
    ) {
        return new StoreLoginResponse(accessToken, nickname, role);
    }
}
