package com.napzak.domain.push.crud;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.common.exception.NapzakException;
import com.napzak.domain.push.code.PushErrorCode;
import com.napzak.domain.push.entity.PushDeviceTokenEntity;
import com.napzak.domain.push.repository.PushDeviceTokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushDeviceTokenUpdater {

	private final PushDeviceTokenRepository pushDeviceTokenRepository;

	@Transactional
	public void updateAllowMessage(Long storeId, String deviceToken, boolean allowMessage) {
		PushDeviceTokenEntity entity = pushDeviceTokenRepository.findByStoreIdAndDeviceToken(storeId, deviceToken)
			.orElseThrow(() -> new NapzakException(PushErrorCode.PUSH_TOKEN_NOT_FOUND));
		entity.updateAllowMessage(allowMessage);
		pushDeviceTokenRepository.save(entity);
	}
}
