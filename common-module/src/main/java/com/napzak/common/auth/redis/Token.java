package com.napzak.common.auth.redis;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@RedisHash(value = "refreshToken", timeToLive = 1209600)
@Getter
@Builder
public class Token {

	@Id
	private Long id;

	@Indexed
	private String refreshToken;

	public static Token of(final Long id, final String refreshToken) {
		return Token.builder()
			.id(id)
			.refreshToken(refreshToken)
			.build();
	}
}
