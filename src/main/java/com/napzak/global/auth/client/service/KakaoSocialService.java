package com.napzak.global.auth.client.service;

import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;
import com.napzak.global.auth.client.dto.StoreSocialLoginRequest;
import com.napzak.global.auth.client.exception.OAuthErrorCode;
import com.napzak.global.auth.client.kakao.KakaoApiClient;
import com.napzak.global.auth.client.kakao.KakaoAuthApiClient;
import com.napzak.global.auth.client.kakao.dto.KakaoAccessTokenResponse;
import com.napzak.global.auth.client.kakao.dto.KakaoUserResponse;
import com.napzak.global.common.exception.NapzakException;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoSocialService implements SocialService {

	private static final String AUTH_CODE = "authorization_code";

	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String clientId;

	private final KakaoApiClient kakaoApiClient;
	private final KakaoAuthApiClient kakaoAuthApiClient;

	@Transactional
	@Override
	public StoreSocialInfoResponse login(
		final String authorizationCode,
		final StoreSocialLoginRequest loginRequest
	) {
		log.info("Attempting Kakao login - Authorization Code: {}, SocialType: {}", authorizationCode,
			loginRequest.socialType());

		// 환경 변수 로그 확인
		log.info("Kakao clientId: {}", clientId);
		log.info("Kakao redirectUri: {}", redirectUri);

		String accessToken;
		try {
			accessToken = getOAuth2Authentication(authorizationCode);
			log.info("Successfully received access token: {}", accessToken);
		} catch (FeignException e) {
			log.error("Failed to retrieve access token from Kakao. Error: {}", e.contentUTF8(), e);
			throw new NapzakException(OAuthErrorCode.O_AUTH_TOKEN_ERROR);
		}

		return getLoginDto(loginRequest.socialType(), getUserInfo(accessToken));
	}

	public StoreSocialInfoResponse loginWithAccessToken(String kakaoAccessToken) {

		KakaoUserResponse kakaoUserResponse = getUserInfo("Bearer " + kakaoAccessToken);
		return getLoginDto(SocialType.KAKAO, kakaoUserResponse);
	}

	private String getOAuth2Authentication(
		final String authorizationCode
	) {
		log.info("Requesting OAuth2 access token - Authorization Code: {}", authorizationCode);

		KakaoAccessTokenResponse response;
		try {
			response = kakaoAuthApiClient.getOAuth2AccessToken(
				AUTH_CODE,
				clientId,
				redirectUri,
				authorizationCode
			);
			log.info("Received Kakao access token successfully: {}", response.accessToken());
		} catch (FeignException e) {
			log.error("Error while requesting OAuth2 access token from Kakao: {}", e.contentUTF8(), e);
			throw new NapzakException(OAuthErrorCode.O_AUTH_TOKEN_ERROR);
		}
		return "Bearer " + response.accessToken();
	}

	private KakaoUserResponse getUserInfo(
		final String accessToken
	) {
		log.info("Fetching user info from Kakao API using access token");

		KakaoUserResponse response;
		try {
			response = kakaoApiClient.getUserInformation(accessToken);
			log.info("Successfully retrieved user info: ID = {}", response.id());
		} catch (FeignException e) {
			log.error("Failed to retrieve user info from Kakao API. Error: {}", e.contentUTF8(), e);
			throw new NapzakException(OAuthErrorCode.GET_INFO_ERROR);
		}
		return response;
	}

	private StoreSocialInfoResponse getLoginDto(
		final SocialType socialType,
		final KakaoUserResponse kakaoUserResponse
	) {
		return StoreSocialInfoResponse.of(
			String.valueOf(kakaoUserResponse.id()),
			socialType
		);
	}

}
