package com.napzak.domain.store.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.core.GenrePreferenceRemover;
import com.napzak.domain.store.core.GenrePreferenceRetriever;
import com.napzak.domain.store.core.GenrePreferenceSaver;
import com.napzak.domain.store.core.StoreSaver;
import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.Store;
import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreRegistrationService {

	private final StoreSaver storeSaver;
	private final GenrePreferenceSaver genrePreferenceSaver;
	private final GenrePreferenceRemover genrePreferenceRemover;
	private final GenrePreferenceRetriever genrePreferenceRetriever;

	@Transactional
	public Long registerStoreWithStoreInfo(final StoreSocialInfoResponse storeSocialInfoResponse) {

		String socialId = storeSocialInfoResponse.socialId();
		SocialType socialType = storeSocialInfoResponse.socialType();

		Store store = storeSaver.save(null, null, Role.ONBOARDING, null, socialId, socialType, null, null);

		log.info("Store registered with storeId: {}, role: {}", store.getId(), store.getRole());

		return store.getId();
	}

	@Transactional
	public void registerGenrePreference(
		Long currentStoreId,
		List<Long> genrePreferenceList
	) {
		genrePreferenceRemover.removeGenrePreference(currentStoreId);

		if (!genrePreferenceList.isEmpty()) {
			genrePreferenceSaver.save(genrePreferenceList, currentStoreId);
		}

	}
}