package com.napzak.domain.external.core.vo;

import com.napzak.domain.external.core.entity.UseTermsEntity;

import lombok.Getter;

@Getter
public class UseTerms {
	private final Long id;
	private final String termsTitle;
	private final String termsUrl;

	public UseTerms(Long id, String termsTitle, String termsUrl) {
		this.id = id;
		this.termsTitle = termsTitle;
		this.termsUrl = termsUrl;
	}

	public static UseTerms fromEntity(UseTermsEntity useTermsEntity) {
		return new UseTerms(
			useTermsEntity.getId(),
			useTermsEntity.getTermsTitle(),
			useTermsEntity.getTermsUrl());
	}
}
