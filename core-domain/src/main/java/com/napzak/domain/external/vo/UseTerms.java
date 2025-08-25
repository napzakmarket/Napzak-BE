package com.napzak.domain.external.vo;

import com.napzak.domain.external.entity.UseTermsEntity;
import com.napzak.domain.external.entity.enums.TermsType;

import lombok.Getter;

@Getter
public class UseTerms {
	private final Long id;
	private final int bundleId;
	private final TermsType termsTitle;
	private final String termsUrl;
	private final boolean isRequired;

	public UseTerms(Long id, int bundleId, TermsType termsTitle, String termsUrl, boolean isRequired) {
		this.id = id;
		this.bundleId = bundleId;
		this.termsTitle = termsTitle;
		this.termsUrl = termsUrl;
		this.isRequired = isRequired;
	}

	public static UseTerms fromEntity(UseTermsEntity useTermsEntity) {
		return new UseTerms(
			useTermsEntity.getId(),
			useTermsEntity.getBundleId(),
			useTermsEntity.getTermsTitle(),
			useTermsEntity.getTermsUrl(),
			useTermsEntity.isRequired());
	}
}
