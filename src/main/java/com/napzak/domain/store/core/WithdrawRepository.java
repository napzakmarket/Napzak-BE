package com.napzak.domain.store.core;

import org.springframework.data.jpa.repository.JpaRepository;

import com.napzak.domain.store.core.entity.WithdrawEntity;

public interface WithdrawRepository extends JpaRepository<WithdrawEntity, Long> {
}
