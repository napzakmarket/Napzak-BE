package com.napzak.domain.store.core;

import org.springframework.stereotype.Component;

import com.napzak.domain.store.api.exception.StoreErrorCode;
import com.napzak.domain.store.core.entity.StoreEntity;
import com.napzak.domain.store.core.vo.Store;
import com.napzak.global.common.exception.NapzakException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class StoreUpdater {

	private final StoreRepository storeRepository;

	public Store updateStoreInfo(
		Long storeId,
		String nickname,
		String phoneNumber,
		String description,
		String photo
	) {
		StoreEntity storeEntity = storeRepository.findById(storeId)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_REGISTERED));

		storeEntity.update(nickname, phoneNumber, description, photo);

		return Store.fromEntity(storeEntity);
	}

}
