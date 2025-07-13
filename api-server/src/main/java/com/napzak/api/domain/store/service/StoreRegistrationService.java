package com.napzak.api.domain.store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.crud.genrepreference.GenrePreferenceRemover;
import com.napzak.domain.store.crud.genrepreference.GenrePreferenceRetriever;
import com.napzak.domain.store.crud.genrepreference.GenrePreferenceSaver;
import com.napzak.domain.store.crud.store.StoreSaver;
import com.napzak.common.auth.client.dto.StoreSocialInfoResponse;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.auth.client.enums.SocialType;
import com.napzak.domain.store.vo.Store;

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