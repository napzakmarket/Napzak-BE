package com.napzak.domain.external.entity;

import com.napzak.domain.external.entity.enums.TermsType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = UseTermsTableConstants.TABLE_USE_TERMS)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UseTermsEntity {

	@Id
	@Column(name = UseTermsTableConstants.COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = UseTermsTableConstants.COLUMN_BUNDLE_ID)
	private int bundleId;

	@Enumerated(EnumType.STRING)
	@Column(name = UseTermsTableConstants.COLUMN_TERMS_TITLE)
	private TermsType termsTitle;

	@Column(name = UseTermsTableConstants.COLUMN_TERMS_URL)
	private String termsUrl;

	@Column(name = UseTermsTableConstants.COLUMN_IS_REQUIRED)
	private boolean isRequired;

	@Builder
	public UseTermsEntity(int bundleId, TermsType termsTitle, String termsUrl, boolean isRequired) {
		this.bundleId = bundleId;
		this.termsTitle = termsTitle;
		this.termsUrl = termsUrl;
		this.isRequired = isRequired;
	}

	public static UseTermsEntity create(int bundleId, TermsType termsTitle, String termsUrl, boolean isRequired) {
		return new UseTermsEntity(bundleId, termsTitle, termsUrl, isRequired);
	}
}
