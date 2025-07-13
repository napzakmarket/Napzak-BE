package com.napzak.domain.external.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.napzak.domain.external.entity.TermsBundleEntity;

@Repository
public interface TermsBundleRepository extends JpaRepository<TermsBundleEntity, Integer> {
	TermsBundleEntity findByIsActive(Boolean isActive);
}
