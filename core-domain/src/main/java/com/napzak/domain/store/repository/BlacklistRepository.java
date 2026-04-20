package com.napzak.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.napzak.domain.store.entity.BlacklistEntity;

public interface BlacklistRepository extends JpaRepository<BlacklistEntity, Long> {

	boolean existsByPhoneNumberHash(String phoneNumberHash);
}
