package com.napzak.global.auth.client.dto;

import com.napzak.domain.store.core.entity.enums.SocialType;

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
