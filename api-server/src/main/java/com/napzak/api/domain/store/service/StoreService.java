package com.napzak.api.domain.store.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.api.amqp.ChatSystemMessageSender;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.store.code.StoreErrorCode;
import com.napzak.domain.store.crud.genrepreference.GenrePreferenceRemover;
import com.napzak.domain.store.crud.genrepreference.GenrePreferenceRetriever;
import com.napzak.domain.store.crud.storeblock.StoreBlockRemover;
import com.napzak.domain.store.crud.storeblock.StoreBlockRetriever;
import com.napzak.domain.store.crud.storeblock.StoreBlockSaver;
import com.napzak.domain.store.crud.storephoto.StorePhotoRetriever;
import com.napzak.domain.store.crud.storereport.StoreReportSaver;
import com.napzak.domain.store.crud.store.StoreRetriever;
import com.napzak.domain.store.crud.store.StoreUpdater;
import com.napzak.domain.store.crud.termsagreement.TermsAgreementSaver;
import com.napzak.domain.store.crud.withdraw.WithdrawSaver;
import com.napzak.domain.store.crud.slang.SlangRetriever;
import com.napzak.domain.store.entity.enums.PhotoType;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.auth.client.enums.SocialType;
import com.napzak.domain.store.vo.GenrePreference;
import com.napzak.domain.store.vo.Store;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.textfilter.SlangFilter;

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
	private final GenrePreferenceRemover genrePreferenceRemover;
	private final StoreBlockRetriever storeBlockRetriever;
	private final StoreBlockSaver storeBlockSaver;
	private final StoreBlockRemover storeBlockRemover;
	private final ChatSystemMessageSender chatSystemMessageSender;

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
	public Role findRoleByStoreId(final Long storeId) {
		return storeRetriever.findRoleByStoreId(storeId);
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
	public void blockStore(Long blockerStoreId, Long blockedStoreId) {
		if (blockerStoreId.equals(blockedStoreId)) {
			throw new NapzakException(StoreErrorCode.SELF_BLOCK_NOT_ALLOWED);
		}
		if (!storeBlockRetriever.isOpponentStoreBlocked(blockerStoreId, blockedStoreId)) {
			try {
				storeBlockSaver.save(blockerStoreId, blockedStoreId);
			} catch (DataIntegrityViolationException ignore) {
				// 유니크 제약(중복 차단) 충돌 시 멱등 처리
			}
		}
	}

	@Transactional
	public void unblockStore(Long blockerStoreId, Long blockedStoreId) {
		storeBlockRemover.removeStoreBlock(blockerStoreId, blockedStoreId);
	}

	@Transactional(readOnly = true)
	public boolean isOpponentStoreBlocked(Long myStoreId, Long opponentStoreId) {
		return storeBlockRetriever.isOpponentStoreBlocked(myStoreId, opponentStoreId);
	}

	@Transactional
	public void withdraw(Long storeId, String title, String description, List<ChatMessage> messages) {
		withdrawSaver.save(storeId, title, description, LocalDateTime.now());
		storeUpdater.updateWithdraw(storeId);
		genrePreferenceRemover.removeGenrePreference(storeId);
		chatSystemMessageSender.sendSystemMessages(messages);
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