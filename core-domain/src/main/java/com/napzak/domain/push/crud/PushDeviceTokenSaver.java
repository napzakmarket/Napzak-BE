package com.napzak.domain.push.crud;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.push.entity.PushDeviceTokenEntity;
import com.napzak.domain.push.entity.enums.Platform;
import com.napzak.domain.push.repository.PushDeviceTokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushDeviceTokenSaver {

	private final PushDeviceTokenRepository pushDeviceTokenRepository;

	@Transactional
	public void saveOrUpdate(Long storeId, String deviceToken, Platform platform,
		boolean isEnabled, boolean allowMessage) {

		pushDeviceTokenRepository.findByStoreIdAndDeviceToken(storeId, deviceToken)
			.ifPresentOrElse(
				entity -> {
					entity.updateIsEnabled(isEnabled);
					entity.updateAllowMessage(allowMessage);
					pushDeviceTokenRepository.save(entity);
				},
				() -> {
					PushDeviceTokenEntity entity = PushDeviceTokenEntity.create(storeId, deviceToken, platform, isEnabled, allowMessage);
					pushDeviceTokenRepository.save(entity);
				}
			);
	}
}
