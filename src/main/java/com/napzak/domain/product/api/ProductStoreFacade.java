package com.napzak.domain.product.api;

import java.util.List;

import org.springframework.stereotype.Component;

import com.napzak.domain.store.api.dto.StoreStatusDto;
import com.napzak.domain.store.core.GenrePreferenceRetriever;
import com.napzak.domain.store.core.StoreRetriever;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductStoreFacade {

	private final StoreRetriever storeRetriever;
	private final GenrePreferenceRetriever genrePreferenceRetriever;

	public StoreStatusDto findStoreStatusDtoByStoreId(Long storeId) {

		return storeRetriever.getStoreStatusDtoById(storeId);
	}

	public List<Long> getGenrePreferenceIds(Long storeId) {
		return genrePreferenceRetriever.getGenrePreferenceIds(storeId);
	}

	public String getStoreNickname(Long storeId) {
		return storeRetriever.findNicknameByStoreId(storeId);
	}
}

