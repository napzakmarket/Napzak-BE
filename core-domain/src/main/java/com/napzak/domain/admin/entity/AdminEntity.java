package com.napzak.domain.admin.entity;

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

@Table(name = AdminTableConstants.TABLE_ADMIN)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = AdminTableConstants.COLUMN_ID)
	private Long id;

	@Column(name = AdminTableConstants.COLUMN_LOGIN_ID, nullable = false, unique = true)
	private String loginId;

	@Column(name = AdminTableConstants.COLUMN_PASSWORD, nullable = false)
	private String password;

	@Column(name = AdminTableConstants.COLUMN_CREATED_AT, nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	private AdminEntity(String loginId, String password) {
		this.loginId = loginId;
		this.password = password;
	}

	public static AdminEntity create(final String loginId, final String encodedPassword) {
		return AdminEntity.builder()
			.loginId(loginId)
			.password(encodedPassword)
			.build();
	}
}
