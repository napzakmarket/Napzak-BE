package com.napzak.domain.store.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.api.dto.StoreNormalRegisterRequest;
import com.napzak.domain.store.core.GenrePreferenceRemover;
import com.napzak.domain.store.core.GenrePreferenceRetriever;
import com.napzak.domain.store.core.GenrePreferenceSaver;
import com.napzak.domain.store.core.StoreSaver;
import com.napzak.domain.store.core.StoreUpdater;
import com.napzak.domain.store.core.entity.GenrePreferenceEntity;
import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.Store;
import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoreRegistrationService {

	private final StoreSaver storeSaver;
	private final StoreUpdater storeUpdater;
	private final GenrePreferenceSaver genrePreferenceSaver;
	private final GenrePreferenceRemover genrePreferenceRemover;
	private final GenrePreferenceRetriever genrePreferenceRetriever;

	public Long registerStoreWithStoreInfo(final StoreSocialInfoResponse storeSocialInfoResponse) {

		Long socialId = storeSocialInfoResponse.socialId();
		SocialType socialType = storeSocialInfoResponse.socialType();

		Store store = storeSaver.save(null, null, Role.STORE, null, socialId, socialType, null);
		//소셜 로그인 후 추가 정보를 입력받는 뷰가 없으므로 비워두는 걸로 하겠습니다.

		log.info("Store registered with storeId: {}, role: {}", store.getId(), store.getRole());

		return store.getId();
	}

	public Store registerStoreWithNormalInfo(
		Long currentStoreId,
		final StoreNormalRegisterRequest storeNormalRegisterRequest
	) {

		if (genrePreferenceRetriever.existsGenrePreference(currentStoreId)) {
			genrePreferenceRemover.removeGenrePreference(currentStoreId);

		}

		String nickname = storeNormalRegisterRequest.nickname();
		String phoneNumber = storeNormalRegisterRequest.phoneNumber();
		String description = storeNormalRegisterRequest.description();
		String photo = storeNormalRegisterRequest.photo();

		Store store = storeUpdater.updateStoreInfo(currentStoreId, nickname, phoneNumber, description, photo);

		if (!storeNormalRegisterRequest.genrePreferenceList().isEmpty()) {
			storeNormalRegisterRequest.genrePreferenceList().forEach(genreId -> {
				GenrePreferenceEntity genrePreferenceEntity = GenrePreferenceEntity.create(currentStoreId, genreId);
				genrePreferenceSaver.save(genrePreferenceEntity);
			});
		}

		return store;
	}
}