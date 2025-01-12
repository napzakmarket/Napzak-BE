package com.napzak.global.auth.client.dto;

import com.napzak.domain.member.core.SocialType;

public record MemberSocialInfoResponse(
        Long socialId,
        SocialType socialType
) {
        public static MemberSocialInfoResponse of(
                final Long socialId,
                final SocialType socialType
        ){
            return new MemberSocialInfoResponse(socialId, socialType);
        }
}
