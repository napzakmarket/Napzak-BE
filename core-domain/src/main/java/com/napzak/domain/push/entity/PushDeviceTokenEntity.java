package com.napzak.domain.push.entity;

import com.napzak.domain.push.entity.enums.Platform;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
	name = PushDeviceTokenTableConstants.TABLE_PUSH_DEVICE_TOKEN,
	uniqueConstraints = @UniqueConstraint(columnNames = {
		PushDeviceTokenTableConstants.COLUMN_STORE_ID,
		PushDeviceTokenTableConstants.COLUMN_DEVICE_TOKEN
	})
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushDeviceTokenEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = PushDeviceTokenTableConstants.COLUMN_ID)
	private Long id;

	@Column(name = PushDeviceTokenTableConstants.COLUMN_STORE_ID, nullable = false)
	private Long storeId;

	@Column(name = PushDeviceTokenTableConstants.COLUMN_DEVICE_TOKEN, nullable = false)
	private String deviceToken;

	@Enumerated(EnumType.STRING)
	@Column(name = PushDeviceTokenTableConstants.COLUMN_PLATFORM, nullable = false)
	private Platform platform;

	@Column(name = PushDeviceTokenTableConstants.COLUMN_IS_ENABLED, nullable = false)
	private boolean isEnabled;

	@Column(name = PushDeviceTokenTableConstants.COLUMN_ALLOW_MESSAGE, nullable = false)
	private boolean allowMessage;

	@Builder
	private PushDeviceTokenEntity(Long storeId, String deviceToken, Platform platform, boolean isEnabled, boolean allowMessage) {
		this.storeId = storeId;
		this.deviceToken = deviceToken;
		this.platform = platform;
		this.isEnabled = isEnabled;
		this.allowMessage = allowMessage;
	}

	public static PushDeviceTokenEntity create(Long storeId, String deviceToken, Platform platform, boolean isEnabled, boolean allowMessage) {
		return PushDeviceTokenEntity.builder()
			.storeId(storeId)
			.deviceToken(deviceToken)
			.platform(platform)
			.isEnabled(isEnabled)
			.allowMessage(allowMessage)
			.build();
	}

	public void updateAllowMessage(boolean allowMessage) {
		this.allowMessage = allowMessage;
	}

	public void updateIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
}
