package com.napzak.domain.store.core.entity;

import static com.napzak.domain.store.core.entity.SlangTableConstants.*;

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

@Table(name = TABLE_SLANG)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SlangEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_WORD, nullable = false)
	private String word;

	@Column(name = COLUMN_PATTERN, nullable = true)
	private String pattern;

	@Column(name = COLUMN_CREATED_AT, nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	public SlangEntity(String word, String pattern, LocalDateTime createdAt) {
		this.word = word;
		this.pattern = pattern;
		this.createdAt = createdAt;
	}

	public static SlangEntity create(String word, String pattern, LocalDateTime createdAt) {
		return SlangEntity.builder()
			.word(word)
			.pattern(pattern)
			.createdAt(createdAt)
			.build();
	}
}
