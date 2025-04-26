package com.napzak.domain.store.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.api.exception.StoreErrorCode;
import com.napzak.domain.store.core.entity.StoreEntity;
import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.global.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StoreUpdater {

	private final StoreRepository storeRepository;

	@Transactional
	public void registerNicknameAndSetRoleAndPhoto(final Long storeId, final String nickname,
		final String profileUrl, final String coverUrl) {
		StoreEntity storeEntity = storeRepository.findById(storeId)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
		storeEntity.setNickname(nickname);
		storeEntity.setRole(Role.STORE);
		storeEntity.setPhoto(profileUrl);
		storeEntity.setCover(coverUrl);
		storeRepository.save(storeEntity);
	}

	@Transactional
	public void updateProfile(final Long storeId, final String cover, final String photo,
		final String nickname, final String description) {
		StoreEntity storeEntity = storeRepository.findById(storeId)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
		storeEntity.setCover(cover);
		storeEntity.setPhoto(photo);
		storeEntity.setNickname(nickname);
		storeEntity.setDescription(description);
	}

	@Transactional
	public void updateRole(final Long storeId, final Role role) {
		StoreEntity storeEntity = storeRepository.findById(storeId)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
		storeEntity.setRole(role);
		storeRepository.save(storeEntity);
	}
}
