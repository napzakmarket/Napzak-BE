package com.napzak.common.auth.client.dto;

import com.napzak.common.auth.client.enums.Platform;
import com.napzak.common.auth.client.enums.SocialType;

import jakarta.validation.constraints.NotNull;

public record StoreSocialLoginRequest(
        @NotNull(message = "소셜 로그인 종류가 입력되지 않았습니다.")
        SocialType socialType,
        @NotNull(message = "플랫폼 종류가 입력되지 않았습니다.")
        Platform platform
) {
}
