package com.napzak.global.auth.jwt.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.napzak.global.auth.redis.Token;

public interface TokenRepository extends CrudRepository<Token, Long> {

	Optional<Token> findByRefreshToken(final String refreshToken);

	Optional<Token> findById(final Long id);
}
