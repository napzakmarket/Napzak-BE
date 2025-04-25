package com.napzak.domain.store.core.entity;

import static com.napzak.domain.store.core.entity.WithdrawTableConstants.*;

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

	@Builder
	public WithdrawEntity(Long withdrawerId, String title, String description, LocalDateTime createdAt) {
		this.withdrawerId = withdrawerId;
		this.title = title;
		this.description = description;
		this.createdAt = createdAt;
	}

	public static WithdrawEntity create(Long withdrawerId, String title, String description, LocalDateTime createdAt) {
		return WithdrawEntity.builder()
			.withdrawerId(withdrawerId)
			.title(title)
			.description(description)
			.createdAt(createdAt)
			.build();
	}
}