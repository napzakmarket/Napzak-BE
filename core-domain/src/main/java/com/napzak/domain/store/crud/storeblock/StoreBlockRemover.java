package com.napzak.domain.store.crud.storeblock;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.repository.StoreBlockRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class StoreBlockRemover {

	private final StoreBlockRepository storeBlockRepository;

	public void removeStoreBlock(Long myStoreId, Long otherStoreId) {
		storeBlockRepository.deleteByBlockerStoreIdAndBlockedStoreId(myStoreId, otherStoreId);
	}
}
