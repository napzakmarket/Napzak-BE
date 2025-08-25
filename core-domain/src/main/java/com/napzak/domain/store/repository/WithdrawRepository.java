package com.napzak.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.napzak.domain.store.entity.WithdrawEntity;

public interface WithdrawRepository extends JpaRepository<WithdrawEntity, Long> {
}
