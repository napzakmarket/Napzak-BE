package com.napzak.api.domain.store.service.social;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.napzak.common.auth.client.apple.dto.ApplePublicKeyDto;
import com.napzak.common.auth.client.apple.dto.ApplePublicKeyResponse;
import com.napzak.common.auth.client.apple.dto.AppleTokenResponseDto;
import com.napzak.common.auth.client.dto.StoreSocialInfoResponse;
import com.napzak.common.auth.client.dto.StoreSocialLoginRequest;
import com.napzak.common.auth.client.enums.Platform;
import com.napzak.common.auth.client.enums.SocialType;
import com.napzak.common.auth.client.exception.OAuthErrorCode;
import com.napzak.common.exception.NapzakException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleSocialService implements SocialService{

	private static final long THIRTY_DAYS_MS = 30L * 24 * 60 * 60 * 1000;

	@Value("${spring.security.oauth2.apple.client-id.web}")
	private String webClientId;

	@Value("${spring.security.oauth2.apple.client-id.ios}")
	private String iosClientId;

	@Value("${spring.security.oauth2.client.registration.apple.client-secret}")
	private String clientSecret;

	@Value("${spring.security.oauth2.apple.iss}")
	private String teamId;

	@Value("${spring.security.oauth2.apple.private-key}")
	private String privateKey;

	@Override
	public StoreSocialInfoResponse login(String authorizationCode, StoreSocialLoginRequest request) {
		Platform platform = request.platform();

		String clientId = switch (platform) {
			case IOS -> iosClientId;
			case WEB -> webClientId;
		};

		AppleTokenResponseDto appleTokenResponse = getAppleToken(authorizationCode, clientId);


		return getStore(appleTokenResponse);
	}

	public AppleTokenResponseDto getAppleToken(String authorizationCode, String clientId) {
		WebClient webClient = WebClient.builder()
			.baseUrl("https://appleid.apple.com")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
			.build();

		try {
			return webClient.post()
				.uri(uriBuilder -> uriBuilder.path("/auth/token")
					.queryParam("grant_type", "authorization_code")
					.queryParam("client_id", clientId)
					.queryParam("client_secret", makeClientSecretToken(clientId))
					.queryParam("code", authorizationCode)
					.build())
				.retrieve()
				.bodyToMono(AppleTokenResponseDto.class)
				.block();
		} catch (WebClientResponseException e) {
			log.error("[애플로그인 실패]: " + e.getResponseBodyAsString(), e);
			throw e;
		}
	}

	public StoreSocialInfoResponse getStore(AppleTokenResponseDto appleTokenResponseDto) {
		PublicKeyFinder publicKeyFinder = new PublicKeyFinder(getApplePublicKeyList());

		Claims claims = Jwts.parserBuilder()
			.setSigningKeyResolver(publicKeyFinder)
			.build()
			.parseClaimsJws(appleTokenResponseDto.getIdToken())
			.getBody();

		StoreSocialInfoResponse storeSocialInfoResponse = StoreSocialInfoResponse.of(claims.getSubject(),
			SocialType.APPLE);
		return storeSocialInfoResponse;
	}

	private List<ApplePublicKeyDto> getApplePublicKeyList() {
		WebClient webClient = WebClient.builder()
			.baseUrl("https://appleid.apple.com")
			.build();

		ApplePublicKeyResponse applePublicKeyResponse = webClient.get()
			.uri("/auth/keys")
			.retrieve()
			.bodyToMono(ApplePublicKeyResponse.class)
			.block();

		return applePublicKeyResponse.getKeys();
	}

	private String makeClientSecretToken(String clientId) {
		String token = Jwts.builder()
			.setSubject(clientId)
			.setIssuer(teamId)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + THIRTY_DAYS_MS))
			.setAudience("https://appleid.apple.com")
			.setHeaderParam("kid", clientSecret)
			.signWith(getPrivateKey(), SignatureAlgorithm.ES256)
			.compact();

		return token;
	}

	private PrivateKey getPrivateKey() {
		try {
			byte[] privateKeyBytes = Decoders.BASE64.decode(privateKey);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("EC");
			return keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			throw new NapzakException(OAuthErrorCode.APPLE_PRIVATE_KEY_GENERATE_FAILED);
		}
	}
}
