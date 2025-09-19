package com.napzak.domain.store.crud.storeblock;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.entity.StoreBlockEntity;
import com.napzak.domain.store.repository.StoreBlockRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class StoreBlockSaver {

	private final StoreBlockRepository storeBlockRepository;

	public void save(Long myStoreId, Long otherStoreId) {
		final StoreBlockEntity storeBlockEntity = StoreBlockEntity.create(
			myStoreId, otherStoreId
		);
		storeBlockRepository.save(storeBlockEntity);
	}
}
