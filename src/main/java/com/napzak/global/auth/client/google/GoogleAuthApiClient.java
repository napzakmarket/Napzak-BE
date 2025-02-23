package com.napzak.global.auth.client.google;

import com.napzak.global.auth.client.google.dto.GoogleAccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-auth-client", url = "https://oauth2.googleapis.com")
public interface GoogleAuthApiClient {

    @PostMapping("/token")
    GoogleAccessTokenResponse getOAuth2AccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String authorizationCode
    );
}
