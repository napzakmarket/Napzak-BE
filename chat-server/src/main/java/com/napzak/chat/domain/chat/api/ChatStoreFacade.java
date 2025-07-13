package com.napzak.chat.domain.chat.api;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.store.crud.store.StoreRetriever;
import com.napzak.domain.store.vo.Store;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatStoreFacade {

	private final StoreRetriever storeRetriever;

	public Store findStoreByStoreId(Long storeId) {
		return storeRetriever.findStoreByStoreId(storeId);
	}

	public List<Store> findStoresByStoreIds(List<Long> storeIds) {
		return storeRetriever.findStoresByStoreIds(storeIds);
	}

	public String findNicknameByStoreId(Long storeId) {
		return storeRetriever.findNicknameByStoreId(storeId);
	}
}
