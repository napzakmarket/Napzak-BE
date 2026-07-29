package com.napzak.domain.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.napzak.domain.admin.entity.AdminEntity;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

	Optional<AdminEntity> findByLoginId(String loginId);
}
