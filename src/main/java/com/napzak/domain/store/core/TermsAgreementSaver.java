package com.napzak.domain.store.core;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.core.entity.TermsAgreementEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TermsAgreementSaver {

	private final TermsAgreementRepository termsAgreementRepository;

	@Transactional
	public void save(Long storeId, int bundleId) {
		TermsAgreementEntity termsAgreementEntity = TermsAgreementEntity.create(storeId, bundleId, LocalDateTime.now());
		termsAgreementRepository.save(termsAgreementEntity);
	}
}
