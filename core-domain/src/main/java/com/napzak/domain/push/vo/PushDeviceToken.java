package com.napzak.domain.push.vo;

import com.napzak.domain.push.entity.PushDeviceTokenEntity;
import com.napzak.domain.push.entity.enums.Platform;

import lombok.Getter;

@Getter
public class PushDeviceToken {

	private final Long id;
	private final Long storeId;
	private final String deviceToken;
	private final Platform platform;
	private final boolean isEnabled;
	private final boolean allowMessage;

	public PushDeviceToken(Long id, Long storeId, String deviceToken, Platform platform, boolean isEnabled, boolean allowMessage) {
		this.id = id;
		this.storeId = storeId;
		this.deviceToken = deviceToken;
		this.platform = platform;
		this.isEnabled = isEnabled;
		this.allowMessage = allowMessage;
	}

	public static PushDeviceToken fromEntity(PushDeviceTokenEntity entity) {
		return new PushDeviceToken(
			entity.getId(),
			entity.getStoreId(),
			entity.getDeviceToken(),
			entity.getPlatform(),
			entity.isEnabled(),
			entity.isAllowMessage()
		);
	}
}
