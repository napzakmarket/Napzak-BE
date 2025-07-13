package com.napzak.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.napzak.domain.store.entity.TermsAgreementEntity;

@Repository
public interface TermsAgreementRepository extends JpaRepository<TermsAgreementEntity, Long> {
}
