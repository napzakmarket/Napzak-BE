package com.napzak.common.auth.jwt.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.napzak.common.auth.redis.Token;

public interface TokenRepository extends CrudRepository<Token, Long> {

	Optional<Token> findByRefreshToken(@Param("refreshToken") String refreshToken);

	Optional<Token> findById(final Long id);
}
