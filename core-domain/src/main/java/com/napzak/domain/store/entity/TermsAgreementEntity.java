package com.napzak.domain.store.entity;

import static com.napzak.domain.store.entity.TermsAgreementConstants.*;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = TABLE_TERMS_AGREEMENT)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsAgreementEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_STORE_ID)
	private Long storeId;

	@Column(name = COLUMN_BUNDLE_ID)
	private int bundleId;

	@Column(name = COLUMN_CREATED_AT)
	private LocalDateTime createdAt;

	@Builder
	public TermsAgreementEntity(Long storeId, int bundleId, LocalDateTime createdAt) {
		this.storeId = storeId;
		this.bundleId = bundleId;
		this.createdAt = createdAt;
	}

	public static TermsAgreementEntity create(Long storeId, int bundleId, LocalDateTime createdAt) {
		return new TermsAgreementEntity(storeId, bundleId, createdAt);
	}
}
