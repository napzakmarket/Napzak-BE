package com.napzak.domain.push.crud;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.push.repository.PushDeviceTokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushDeviceTokenRemover {

	private final PushDeviceTokenRepository pushDeviceTokenRepository;

	@Transactional
	public void delete(Long storeId, String deviceToken) {
		pushDeviceTokenRepository.deleteByStoreIdAndDeviceToken(storeId, deviceToken);
	}
}
