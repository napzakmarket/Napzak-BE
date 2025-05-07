package com.napzak.domain.store.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.napzak.domain.store.core.entity.TermsAgreementEntity;

@Repository
public interface TermsAgreementRepository extends JpaRepository<TermsAgreementEntity, Long> {
}
