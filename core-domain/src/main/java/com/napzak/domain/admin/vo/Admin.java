package com.napzak.domain.admin.vo;

import java.time.LocalDateTime;

import com.napzak.domain.admin.entity.AdminEntity;

import lombok.Getter;

@Getter
public class Admin {
	private final Long id;
	private final String loginId;
	private final String password;
	private final LocalDateTime createdAt;

	public Admin(
		Long id,
		String loginId,
		String password,
		LocalDateTime createdAt
	) {
		this.id = id;
		this.loginId = loginId;
		this.password = password;
		this.createdAt = createdAt;
	}

	public static Admin fromEntity(AdminEntity adminEntity) {
		return new Admin(
			adminEntity.getId(),
			adminEntity.getLoginId(),
			adminEntity.getPassword(),
			adminEntity.getCreatedAt()
		);
	}
}
