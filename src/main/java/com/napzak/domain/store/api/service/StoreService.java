package com.napzak.domain.store.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.external.core.entity.enums.TermsType;
import com.napzak.domain.external.core.vo.UseTerms;
import com.napzak.domain.store.api.exception.StoreErrorCode;
import com.napzak.domain.store.core.GenrePreferenceRetriever;
import com.napzak.domain.store.core.StorePhotoRetriever;
import com.napzak.domain.store.core.StoreReportSaver;
import com.napzak.domain.store.core.StoreRetriever;
import com.napzak.domain.store.core.StoreUpdater;
import com.napzak.domain.store.core.TermsAgreementSaver;
import com.napzak.domain.store.core.WithdrawSaver;
import com.napzak.domain.store.core.entity.SlangRetriever;
import com.napzak.domain.store.core.entity.enums.PhotoType;
import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.domain.store.core.entity.enums.SocialType;
import com.napzak.domain.store.core.vo.GenrePreference;
import com.napzak.domain.store.core.vo.Store;
import com.napzak.global.common.exception.NapzakException;
import com.napzak.global.common.slang.SlangFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreService {
	private final StoreRetriever storeRetriever;
	private final GenrePreferenceRetriever genrePreferenceRetriever;
	private final SlangRetriever slangRetriever;
	private final SlangFilter slangFilter;
	private final StoreUpdater storeUpdater;
	private final StoreReportSaver storeReportSaver;
	private final WithdrawSaver withdrawSaver;
	private final StorePhotoRetriever storePhotoRetriever;
	private final TermsAgreementSaver termsAgreementSaver;

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

	@Transactional(readOnly = true)
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

	@Transactional
	public void registerNickname(final Long storeId, final String nickname){
		String profileUrl = storePhotoRetriever.getStorePhoto(PhotoType.PROFILE);
		String coverUrl = storePhotoRetriever.getStorePhoto(PhotoType.COVER);
		storeUpdater.registerNicknameAndSetRoleAndPhoto(storeId, nickname, profileUrl, coverUrl);
	}

	@Transactional
	public void modifyProfile(final Long storeId, final String cover, final String photo,
		final String nickname, final String description){
		storeUpdater.updateProfile(storeId, cover, photo, nickname, description);
	}

	@Transactional
	public void reportStore(Long reporterId, Store reportedStore, String title, String description, String contact) {
		storeReportSaver.save(
			reporterId,
			reportedStore,
			title,
			description,
			contact
		);
	}

	@Transactional
	public void withdraw(Long storeId, String title, String description) {
		withdrawSaver.save(storeId, title, description, LocalDateTime.now());
		storeUpdater.updateRole(storeId, Role.WITHDRAWN);
	}

	@Transactional
	public void registerAgreement(Long storeId, int bundleId) {termsAgreementSaver.save(storeId, bundleId);}

	public void syncSlangToRedis() {
		slangRetriever.updateSlangToRedis();
	}

	@Transactional
	public void changeStoreRole(Long storeId, Role role) {
		storeUpdater.updateRole(storeId, role);
	}
}