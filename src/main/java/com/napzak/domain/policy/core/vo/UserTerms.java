package com.napzak.domain.policy.core.vo;

import com.napzak.domain.policy.core.entity.UserTermsEntity;

import lombok.Getter;

@Getter
public class UserTerms {
	private final Long id;
	private final String termsTitle;
	private final String termsUrl;

	public UserTerms(Long id, String termsTitle, String termsUrl) {
		this.id = id;
		this.termsTitle = termsTitle;
		this.termsUrl = termsUrl;
	}

	public static UserTerms fromEntity(UserTermsEntity userTermsEntity) {
		return new UserTerms(
			userTermsEntity.getId(),
			userTermsEntity.getTermsTitle(),
			userTermsEntity.getTermsUrl());
	}
}
