package com.napzak.domain.store.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.api.dto.StoreStatusDto;
import com.napzak.domain.store.api.exception.StoreErrorCode;
import com.napzak.domain.store.core.entity.StoreEntity;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.Store;
import com.napzak.global.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreRetriever {

	private final StoreRepository storeRepository;

	public Store findStoreByStoreId(final Long id) {
		StoreEntity storeEntity = storeRepository.findById(id)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));

		return Store.fromEntity((storeEntity));
	}

	public Store retrieveBySocialTypeAndSocialId(Long socialId, SocialType socialType) {
		StoreEntity storeEntity = storeRepository.findBySocialTypeAndSocialId(socialId, socialType)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
		return Store.fromEntity(storeEntity);
	}

	public Store retrieveStoreByStoreId(Long StoreId) {
		StoreEntity storeEntity = storeRepository.findById(StoreId)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
		return Store.fromEntity(storeEntity);
	}

	public boolean checkStoreExistsBySocialIdAndSocialType(final Long socialId, final SocialType socialType) {
		return storeRepository.findBySocialTypeAndSocialId(socialId, socialType).isPresent();
	}

	public StoreStatusDto getStoreStatusDtoById(final Long storeId) {
		return storeRepository.findStoreStatusById(storeId);
	}

}
