package com.napzak.chat.domain.push.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.push.crud.PushDeviceTokenRemover;
import com.napzak.domain.push.crud.PushDeviceTokenRetriever;
import com.napzak.domain.push.crud.PushDeviceTokenSaver;
import com.napzak.domain.push.crud.PushDeviceTokenUpdater;
import com.napzak.domain.push.entity.enums.Platform;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PushTokenService {

	private final PushDeviceTokenSaver pushDeviceTokenSaver;
	private final PushDeviceTokenUpdater pushDeviceTokenUpdater;
	private final PushDeviceTokenRetriever pushDeviceTokenRetriever;
	private final PushDeviceTokenRemover pushDeviceTokenRemover;

	@Transactional
	public void registerPushToken(Long storeId, String deviceToken, Platform platform, boolean isEnabled, boolean allowMessage) {
		pushDeviceTokenSaver.saveOrUpdate(storeId, deviceToken, platform, isEnabled, allowMessage);
	}

	@Transactional(readOnly = true)
	public boolean findAllowMessageByStoreIdAndDeviceToken(Long storeId, String deviceToken) {
		return pushDeviceTokenRetriever.findAllowMessageByStoreIdAndDeviceToken(storeId, deviceToken);
	}

	@Transactional
	public void updateAllowMessage(Long storeId, String deviceToken, boolean allowMessage) {
		pushDeviceTokenUpdater.updateAllowMessage(storeId, deviceToken, allowMessage);
	}

	@Transactional
	public void deletePushToken(Long storeId, String deviceToken) {
		pushDeviceTokenRemover.delete(storeId, deviceToken);
	}
}
