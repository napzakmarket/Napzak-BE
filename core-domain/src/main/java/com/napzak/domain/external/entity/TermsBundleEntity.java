package com.napzak.domain.external.entity;

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

@Table(name = TermsBundleConstants.TABLE_TERMS_BUNDLE)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsBundleEntity {

	@Id
	@Column(name = TermsBundleConstants.COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = TermsBundleConstants.COLUMN_VERSION)
	private int version;

	@Column(name = TermsBundleConstants.COLUMN_DESCRIPTION)
	private String description;

	@Column(name = TermsBundleConstants.COLUMN_CREATED_AT)
	private LocalDateTime createdAt;

	@Column(name = TermsBundleConstants.COLUMN_IS_ACTIVE)
	private boolean isActive;

	@Builder
	public TermsBundleEntity(int version, String description, LocalDateTime createdAt, boolean isActive) {
		this.version = version;
		this.description = description;
		this.createdAt = createdAt;
		this.isActive = isActive;
	}

	public static TermsBundleEntity create(int version, String description, LocalDateTime createdAt, boolean isActive) {
		return new TermsBundleEntity(version, description, createdAt, isActive);
	}

}
