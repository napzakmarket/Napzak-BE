package com.napzak.global.auth.client.service;

import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;
import com.napzak.global.auth.client.dto.StoreSocialLoginRequest;
import com.napzak.global.auth.client.exception.OAuthErrorCode;
import com.napzak.global.auth.client.google.GoogleApiClient;
import com.napzak.global.auth.client.google.GoogleAuthApiClient;
import com.napzak.global.auth.client.google.dto.GoogleAccessTokenResponse;
import com.napzak.global.auth.client.google.dto.GoogleUserResponse;
import com.napzak.global.common.exception.NapzakException;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleSocialService implements SocialService {

    private static final String AUTH_CODE = "authorization_code";

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    private final GoogleApiClient googleApiClient;
    private final GoogleAuthApiClient googleAuthApiClient;

    @Override
    public StoreSocialInfoResponse login(
            final String authorizationCode,
            final StoreSocialLoginRequest loginRequest
    ){
        String accessToken;
        try {
            accessToken = getOAuth2Authentication(authorizationCode);
        } catch (FeignException e) {
            throw new NapzakException(OAuthErrorCode.O_AUTH_TOKEN_ERROR);
        }

        return getLoginDto(loginRequest.socialType(), getUserInfo(accessToken));
    }

    private String getOAuth2Authentication(
            final String authorizationCode) {
        GoogleAccessTokenResponse response;
        try {
            response = googleAuthApiClient.getOAuth2AccessToken(
                    AUTH_CODE,
                    clientId,
                    clientSecret,
                    redirectUri,
                    authorizationCode
            );
        } catch (FeignException e) {
            throw new NapzakException(OAuthErrorCode.O_AUTH_TOKEN_ERROR);
        }
        return "Bearer " + response.accessToken();
    }

    private GoogleUserResponse getUserInfo(
            final String accessToken
    ) {
        log.info("Fetching user info from Google API using access token");

        GoogleUserResponse response;
        try {
            response = googleApiClient.getUserInformation(accessToken);
            log.info("Successfully retrieved user info: ID = {}", response.sub());
        } catch (FeignException e) {
            log.error("Failed to retrieve user info from Google API. Error: {}", e.contentUTF8(), e);
            throw new NapzakException(OAuthErrorCode.GET_INFO_ERROR);
        }
        return response;
    }

    private StoreSocialInfoResponse getLoginDto(
            final SocialType socialType,
            final GoogleUserResponse googleUserResponse
    ) {
        return StoreSocialInfoResponse.of(
                googleUserResponse.sub(),
                socialType
        );
    }
}
