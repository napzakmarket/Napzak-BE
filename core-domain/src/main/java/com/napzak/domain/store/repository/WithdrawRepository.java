package com.napzak.domain.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.napzak.domain.store.entity.WithdrawEntity;

public interface WithdrawRepository extends JpaRepository<WithdrawEntity, Long> {

	boolean existsByPhoneNumberHashAndBlacklistedTrue(String phoneNumberHash);
}
