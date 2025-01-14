package com.napzak.global.auth.client.service;

import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;
import com.napzak.global.auth.client.dto.StoreSocialLoginRequest;

public interface SocialService {
    StoreSocialInfoResponse login(
            final String authorizationToken,
            final StoreSocialLoginRequest storeSocialLoginRequest);
}
