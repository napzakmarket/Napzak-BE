package com.napzak.domain.policy.core.vo;

import com.napzak.domain.policy.core.entity.UserTermsEntity;

import lombok.Getter;

@Getter
public class UserTerms {
	private final Long id;
	private final String termTitle;
	private final String termUrl;

	public UserTerms(Long id, String termTitle, String termUrl) {
		this.id = id;
		this.termTitle = termTitle;
		this.termUrl = termUrl;
	}

	public static UserTerms fromEntity(UserTermsEntity userTermEntity) {
		return new UserTerms(
			userTermEntity.getId(),
			userTermEntity.getTermTitle(),
			userTermEntity.getTermUrl());
	}
}
