package com.napzak.domain.external.core.entity;

import static com.napzak.domain.external.core.entity.ConfigConstants.*;

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

@Table(name = TABLE_CONFIG)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfigEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_CONFIG_KEY)
	private String configKey;

	@Column(name = COLUMN_CONFIG_VALUE)
	private String configValue;

	@Column(name = COLUMN_UPDATED_AT)
	private LocalDateTime updatedAt;

	@Builder
	private ConfigEntity(String configKey, String configValue) {
		this.configKey = configKey;
		this.configValue = configValue;
	}

	private ConfigEntity create(String configKey, String configValue) {
		return new ConfigEntity(configKey, configValue);
	}
}
