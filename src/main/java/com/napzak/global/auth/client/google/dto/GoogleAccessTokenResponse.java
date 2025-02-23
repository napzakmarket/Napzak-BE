package com.napzak.global.auth.client.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleAccessTokenResponse(
        String accessToken
) {
    public static GoogleAccessTokenResponse of(
            final String accessToken
    ) {
        return new GoogleAccessTokenResponse(
                accessToken
        );
    }
}
