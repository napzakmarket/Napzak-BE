package com.napzak.global.auth.client.service;

import com.napzak.global.auth.client.dto.MemberSocialInfoResponse;
import com.napzak.global.auth.client.dto.MemberLoginRequest;

public interface SocialService {
    MemberSocialInfoResponse login(
            final String authorizationToken,
            final MemberLoginRequest memberLoginRequest);
}
