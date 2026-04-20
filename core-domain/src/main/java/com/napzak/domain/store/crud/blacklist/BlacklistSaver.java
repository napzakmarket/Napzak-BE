package com.napzak.domain.store.crud.blacklist;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.entity.BlacklistEntity;
import com.napzak.domain.store.repository.BlacklistRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BlacklistSaver {

	private final BlacklistRepository blacklistRepository;

	@Transactional
	public void save(Long storeId, String phoneNumberEnc, String phoneNumberHash) {
		if (phoneNumberHash != null && blacklistRepository.existsByPhoneNumberHash(phoneNumberHash)) {
			return;
		}
		blacklistRepository.save(BlacklistEntity.create(storeId, phoneNumberEnc, phoneNumberHash));
	}
}