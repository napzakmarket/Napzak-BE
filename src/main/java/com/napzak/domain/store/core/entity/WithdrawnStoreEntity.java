package com.napzak.domain.store.core.entity;

import static com.napzak.domain.store.core.entity.WithdrawnStoreTableConstants.*;

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

@Table(name = TABLE_WITHDRAWN_STORE)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawnStoreEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_TITLE, nullable = false)
	private String title;

	@Column(name = COLUMN_DESCRIPTION)
	private String description;

	@Column(name = COLUMN_CREATED_AT, nullable = false)
	private LocalDateTime createdAt;

	@Builder
	public WithdrawnStoreEntity(String title, String description, LocalDateTime createdAt) {
		this.title = title;
		this.description = description;
		this.createdAt = createdAt;
	}

	public static WithdrawnStoreEntity create(String title, String description, LocalDateTime createdAt) {
		return WithdrawnStoreEntity.builder()
			.title(title)
			.description(description)
			.createdAt(createdAt)
			.build();
	}
}