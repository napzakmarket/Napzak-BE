package com.napzak.domain.store.entity;

import static com.napzak.domain.store.entity.WithdrawTableConstants.*;

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

@Table(name = TABLE_WITHDRAW)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_WITHDRAWER_ID)
	private Long withdrawerId;

	@Column(name = COLUMN_TITLE, nullable = false)
	private String title;

	@Column(name = COLUMN_DESCRIPTION, columnDefinition = "TEXT")
	private String description;

	@Column(name = COLUMN_CREATED_AT, nullable = false)
	private LocalDateTime createdAt;

	@Column(name = COLUMN_PHONE_NUMBER_ENC, nullable = true)
	private String phoneNumberEnc;

	@Column(name = COLUMN_PHONE_NUMBER_HASH, nullable = true)
	private String phoneNumberHash;

	@Column(name = COLUMN_BLACKLISTED, nullable = false)
	private boolean blacklisted;

	@Builder
	public WithdrawEntity(Long withdrawerId, String title, String description, LocalDateTime createdAt,
		String phoneNumberEnc, String phoneNumberHash, boolean blacklisted) {
		this.withdrawerId = withdrawerId;
		this.title = title;
		this.description = description;
		this.createdAt = createdAt;
		this.phoneNumberEnc = phoneNumberEnc;
		this.phoneNumberHash = phoneNumberHash;
		this.blacklisted = blacklisted;
	}

	public static WithdrawEntity create(Long withdrawerId, String title, String description, LocalDateTime createdAt,
		String phoneNumberEnc, String phoneNumberHash, boolean blacklisted) {
		return WithdrawEntity.builder()
			.withdrawerId(withdrawerId)
			.title(title)
			.description(description)
			.createdAt(createdAt)
			.phoneNumberEnc(phoneNumberEnc)
			.phoneNumberHash(phoneNumberHash)
			.blacklisted(blacklisted)
			.build();
	}
}