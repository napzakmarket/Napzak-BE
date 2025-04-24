package com.napzak.domain.store.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.api.exception.StoreErrorCode;
import com.napzak.domain.store.core.GenrePreferenceRetriever;
import com.napzak.domain.store.core.StoreRetriever;
import com.napzak.domain.store.core.entity.SlangRetriever;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.GenrePreference;
import com.napzak.domain.store.core.vo.Store;
import com.napzak.global.common.exception.NapzakException;
import com.napzak.global.common.slang.SlangFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {
	private final StoreRetriever storeRetriever;
	private final GenrePreferenceRetriever genrePreferenceRetriever;
	private final SlangRetriever slangRetriever;
	private final SlangFilter slangFilter;

	@Transactional(readOnly = true)
	public Store findStoreByStoreId(Long StoreId) {
		return storeRetriever.retrieveStoreByStoreId(StoreId);

	}

	@Transactional(readOnly = true)
	public boolean checkStoreExistsBySocialIdAndSocialType(final String socialId, final SocialType socialType) {
		return storeRetriever.checkStoreExistsBySocialIdAndSocialType(socialId, socialType);
	}

	@Transactional(readOnly = true)
	public Store findStoreBySocialIdAndSocialType(final String socialId, final SocialType socialType) {
		return storeRetriever.retrieveBySocialTypeAndSocialId(socialId, socialType);
	}

	@Transactional(readOnly = true)
	public Store getStore(final Long storeId) {

		if (!storeRetriever.existsById(storeId)) {
			throw new NapzakException(StoreErrorCode.STORE_NOT_FOUND);
		}

		return storeRetriever.findStoreByStoreId(storeId);

	}

	public List<GenrePreference> getGenrePreferenceList(Long storeId) {
		return genrePreferenceRetriever.getGenrePreferences(storeId);
	}

	@Transactional(readOnly = true)
	public void validateNickname(String nickname) {
		if (storeRetriever.existsByNickname(nickname)) {
			throw new NapzakException(StoreErrorCode.DUPLICATE_NICKNAME);
		}

		if (slangFilter.containsSlang(nickname)) {
			throw new NapzakException(StoreErrorCode.NICKNAME_CONTAINS_SLANG);
		}
	}

	public void syncSlangToRedis() {
		slangRetriever.updateSlangToRedis();
	}
}