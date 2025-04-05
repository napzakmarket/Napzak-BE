package com.napzak.domain.store.core.vo;

import com.napzak.domain.store.core.entity.SlangEntity;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Slang {
	private final Long id;
	private final String word;
	private final String pattern;
	private final LocalDateTime createdAt;

	public Slang(Long id, String word, String pattern, LocalDateTime createdAt) {
		this.id = id;
		this.word = word;
		this.pattern = pattern;
		this.createdAt = createdAt;
	}

	public static Slang fromEntity(SlangEntity slangEntity) {
		return new Slang(
			slangEntity.getId(),
			slangEntity.getWord(),
			slangEntity.getPattern(),
			slangEntity.getCreatedAt()
		);
	}
}
