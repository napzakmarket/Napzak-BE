package com.napzak.global.common.config;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EnvironmentLogger {

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;

	@Value("${spring.data.redis.host}")
	private String redisHost;

	@Value("${spring.data.redis.port}")
	private String redisPort;

	@Value("${spring.datasource.url}")
	private String databaseUrl;

	@PostConstruct
	public void logEnvironment() {
		log.info("============= ENVIRONMENT VARIABLES =============");
		log.info("Kakao Client ID: {}", clientId);
		log.info("Kakao Redirect URI: {}", redirectUri);
		log.info("Redis Host: {}", redisHost);
		log.info("Redis Port: {}", redisPort);
		log.info("Database URL: {}", databaseUrl);
		log.info("=================================================");
	}
}