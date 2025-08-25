package com.napzak.common.auth.client.dto;

import com.napzak.common.auth.client.enums.SocialType;

public record StoreSocialInfoResponse(
        String socialId,
        SocialType socialType
) {
        public static StoreSocialInfoResponse of(
                final String socialId,
                final SocialType socialType
        ){
            return new StoreSocialInfoResponse(socialId, socialType);
        }
}
