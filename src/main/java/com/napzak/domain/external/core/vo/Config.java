package com.napzak.domain.external.core.vo;

import java.time.LocalDateTime;

import com.napzak.domain.external.core.entity.ConfigEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Config {
	private final Long id;
	private final String configKey;
	private final String configValue;
	private final LocalDateTime updatedAt;

	public static Config fromEntity(ConfigEntity configEntity) {
		return new Config(
			configEntity.getId(),
			configEntity.getConfigKey(),
			configEntity.getConfigValue(),
			configEntity.getUpdatedAt()
		);
	}
}
