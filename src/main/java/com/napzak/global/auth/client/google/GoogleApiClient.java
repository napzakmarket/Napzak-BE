package com.napzak.global.auth.client.google;

import com.napzak.global.auth.client.google.dto.GoogleUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "google-api-client", url = "https://www.googleapis.com/oauth2/v3")
public interface GoogleApiClient {

    @GetMapping("/userinfo")
    GoogleUserResponse getUserInformation(@RequestHeader("Authorization") String authorization);
}
