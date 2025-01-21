package com.napzak.domain.store.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.core.GenrePreferenceRemover;
import com.napzak.domain.store.core.GenrePreferenceRetriever;
import com.napzak.domain.store.core.GenrePreferenceSaver;
import com.napzak.domain.store.core.StoreSaver;
import com.napzak.domain.store.core.StoreUpdater;
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
		List<Long> genrePreferenceList,
		String nickname,
		String phoneNumber,
		String description,
		String photo
	) {

		if (genrePreferenceRetriever.existsGenrePreference(currentStoreId)) {
			genrePreferenceRemover.removeGenrePreference(currentStoreId);

		}
		Store store = storeUpdater.updateStoreInfo(currentStoreId, nickname, phoneNumber, description, photo);

		if (!genrePreferenceList.isEmpty()) {
			genrePreferenceSaver.save(genrePreferenceList, currentStoreId);
		}

		return store;
	}
}