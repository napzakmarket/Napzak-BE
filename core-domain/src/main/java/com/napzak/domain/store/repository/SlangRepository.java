package com.napzak.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.napzak.domain.store.entity.SlangEntity;

public interface SlangRepository extends JpaRepository<SlangEntity, Long> {
}
