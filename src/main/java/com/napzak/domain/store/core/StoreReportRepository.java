package com.napzak.domain.store.core;

import org.springframework.data.jpa.repository.JpaRepository;

import com.napzak.domain.store.core.entity.StoreReportEntity;

public interface StoreReportRepository extends JpaRepository<StoreReportEntity, Long> {
}
