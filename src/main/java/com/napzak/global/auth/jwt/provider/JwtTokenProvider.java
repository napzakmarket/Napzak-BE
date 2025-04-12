package com.napzak.global.auth.jwt.provider;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.napzak.domain.store.core.entity.enums.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.access-token-expire-time}")
	private long accessTokenExpireTime;

	@Value("${jwt.refresh-token-expire-time}")
	private long refreshTokenExpireTime;

	private static final String STORE_ID = "storeId";
	private static final String ROLE_KEY = "role";

	@PostConstruct
	protected void init() {
		jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	public String issueAccessToken(final Authentication authentication) {
		return issueToken(authentication, accessTokenExpireTime);
	}

	public String issueRefreshToken(final Authentication authentication) {
		return issueToken(authentication, refreshTokenExpireTime);
	}

	public JwtValidationType validateToken(String token) {
		try {
			Claims claims = getBody(token);
			return JwtValidationType.VALID_JWT;
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT Token: {}", ex.getMessage());
			return JwtValidationType.INVALID_JWT_TOKEN;
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT Token: {}", ex.getMessage());
			return JwtValidationType.EXPIRED_JWT_TOKEN;
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT Token: {}", ex.getMessage());
			return JwtValidationType.UNSUPPORTED_JWT_TOKEN;
		} catch (IllegalArgumentException ex) {
			log.error("Empty JWT Token or Illegal Argument: {}", ex.getMessage());
			return JwtValidationType.EMPTY_JWT;
		} catch (SignatureException ex) {
			log.error("Invalid JWT Signature: {}", ex.getMessage());
			return JwtValidationType.INVALID_JWT_SIGNATURE;
		}
	}

	public Long getStoreIdFromJwt(String token) {
		Claims claims = getBody(token);
		Long storeId = Long.valueOf(claims.get(STORE_ID).toString());

		// 로그 추가: storeId 확인
		log.info("Extracted storeId from JWT: {}", storeId);

		return storeId;
	}

	public Role getRoleFromJwt(String token) {
		Claims claims = getBody(token);
		String roleName = claims.get(ROLE_KEY, String.class);

		log.info("Extracted role from JWT: {}", roleName);

		// "ROLE_" 접두사 제거
		String enumValue = roleName.replace("ROLE_", "");
		log.info("Final role after processing: {}", enumValue);

		return Role.valueOf(enumValue.toUpperCase());
	}

	private String issueToken(final Authentication authentication, final long expiredTime) {
		final Date now = new Date();

		final Claims claims = Jwts.claims().setIssuedAt(now).setExpiration(new Date(now.getTime() + expiredTime));

		claims.put(STORE_ID, authentication.getPrincipal());
		log.info("Added store ID to claims: {}", authentication.getPrincipal());
		log.info("Authorities before token generation: {}", authentication.getAuthorities());

		String role = authentication.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No authorities found for user"));

		log.info("Selected role for token: {}", role);

		claims.put(ROLE_KEY, role);
		log.info("Added role to claims: {}", role);

		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setClaims(claims)
			.signWith(getSigningKey())
			.compact();
	}

	private Claims getBody(final String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	private SecretKey getSigningKey() {
		String encodedKey = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
		return Keys.hmacShaKeyFor(encodedKey.getBytes());
	}

}
