package com.napzak.domain.external.core.entity;

import static com.napzak.domain.external.core.entity.UseTermsTableConstants.*;

import com.napzak.domain.external.core.entity.enums.TermsType;

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

@Table(name = TABLE_USE_TERMS)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UseTermsEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_BUNDLE_ID)
	private int bundleId;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_TERMS_TITLE)
	private TermsType termsTitle;

	@Column(name = COLUMN_TERMS_URL)
	private String termsUrl;

	@Column(name = COLUMN_IS_REQUIRED)
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
