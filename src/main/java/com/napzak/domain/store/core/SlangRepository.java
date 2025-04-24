package com.napzak.domain.store.core;

import org.springframework.data.jpa.repository.JpaRepository;

import com.napzak.domain.store.core.entity.SlangEntity;

public interface SlangRepository extends JpaRepository<SlangEntity, Long> {
}
