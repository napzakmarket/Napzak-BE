package com.napzak.api.domain.product;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.store.vo.Store;
import com.napzak.domain.store.vo.StoreStatus;
import com.napzak.domain.store.crud.genrepreference.GenrePreferenceRetriever;
import com.napzak.domain.store.crud.store.StoreRetriever;
import com.napzak.common.auth.role.enums.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductStoreFacade {

	private final StoreRetriever storeRetriever;
	private final GenrePreferenceRetriever genrePreferenceRetriever;

	public Store getStoreById(Long storeId) {
		return storeRetriever.findStoreByStoreId(storeId);
	}

	public StoreStatus getStoreStatusByStoreId(Long storeId) {

		return storeRetriever.getStoreStatusDtoById(storeId);
	}

	public List<Long> getGenrePreferenceIds(Long storeId) {
		return genrePreferenceRetriever.getGenrePreferenceIds(storeId);
	}

	public String getStoreNickname(Long storeId) {
		return storeRetriever.findNicknameByStoreId(storeId);
	}

	public Role getStoreRole(Long storeId) { return storeRetriever.findStoreByStoreId(storeId).getRole();}
}

