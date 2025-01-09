package com.napzak.global.auth.client.service;

import com.napzak.global.auth.client.dto.MemberInfoResponse;
import com.napzak.global.auth.client.dto.MemberLoginRequest;

public interface SocialService {
    MemberInfoResponse login(
            final String authorizationToken,
            final MemberLoginRequest loginRequest);
}
