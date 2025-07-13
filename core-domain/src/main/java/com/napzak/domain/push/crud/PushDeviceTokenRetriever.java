package com.napzak.domain.push.crud;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.common.exception.NapzakException;
import com.napzak.domain.push.code.PushErrorCode;
import com.napzak.domain.push.repository.PushDeviceTokenRepository;
import com.napzak.domain.push.vo.PushDeviceToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushDeviceTokenRetriever {

	private final PushDeviceTokenRepository pushDeviceTokenRepository;

	@Transactional(readOnly = true)
	public PushDeviceToken findByStoreIdAndDeviceToken(Long storeId, String deviceToken) {
		return pushDeviceTokenRepository.findByStoreIdAndDeviceToken(storeId, deviceToken)
			.map(PushDeviceToken::fromEntity)
			.orElseThrow(() -> new NapzakException(PushErrorCode.PUSH_TOKEN_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public boolean findAllowMessageByStoreIdAndDeviceToken(Long storeId, String deviceToken) {
		return pushDeviceTokenRepository.findAllowMessageByStoreIdAndDeviceToken(storeId, deviceToken);
	}

	@Transactional(readOnly = true)
	public List<String> findAllowMessageDeviceTokensByStoreId(Long storeId) {
		return pushDeviceTokenRepository.findAllowMessageDeviceTokensByStoreId(storeId);
	}
}
