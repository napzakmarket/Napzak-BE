package com.napzak.common.auth.redis;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@RedisHash(value = "refreshToken", timeToLive = 1209600)
@Getter
public class Token {

	@Id
	private Long id;

	@Indexed
	private String refreshToken;

	public Token() {}

	public Token(Long id, String refreshToken) {
		this.id = id;
		this.refreshToken = refreshToken;
	}

	public static Token of(Long id, String refreshToken) {
		return new Token(id, refreshToken);
	}
}
