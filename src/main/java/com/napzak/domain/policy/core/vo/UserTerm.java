package com.napzak.domain.policy.core.vo;

import com.napzak.domain.policy.core.entity.UserTermEntity;

import lombok.Getter;

@Getter
public class UserTerm {
	private final Long id;
	private final String termTitle;
	private final String termUrl;

	public UserTerm(Long id, String termTitle, String termUrl) {
		this.id = id;
		this.termTitle = termTitle;
		this.termUrl = termUrl;
	}

	public static UserTerm fromEntity(UserTermEntity userTermEntity) {
		return new UserTerm(
			userTermEntity.getId(),
			userTermEntity.getTermTitle(),
			userTermEntity.getTermUrl());
	}
}
