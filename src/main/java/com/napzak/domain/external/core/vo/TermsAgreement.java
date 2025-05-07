package com.napzak.domain.external.core.vo;

import java.time.LocalDateTime;

import com.napzak.domain.external.core.entity.TermsAgreementEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TermsAgreement {
	private final Long id;
	private final Long storeId;
	private final int bundleId;
	private final LocalDateTime createdAt;

	public static TermsAgreement fromEntity(TermsAgreementEntity termsAgreementEntity) {
		return new TermsAgreement(
			termsAgreementEntity.getId(),
			termsAgreementEntity.getStoreId(),
			termsAgreementEntity.getBundleId(),
			termsAgreementEntity.getCreatedAt()
		);
	}
}
