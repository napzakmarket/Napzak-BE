package com.napzak.domain.store.crud.store;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.common.util.encryption.PhoneEncryptionUtil;
import com.napzak.common.util.slack.SlackWebhookSender;
import com.napzak.domain.store.code.StoreErrorCode;
import com.napzak.domain.store.entity.StoreEntity;
import com.napzak.domain.store.repository.StoreRepository;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StoreUpdater {

	private final StoreRepository storeRepository;
	private final PhoneEncryptionUtil phoneEncryptionUtil;
	private final SlackWebhookSender slackWebhookSender;

	@Transactional
	public void registerNicknameAndSetRoleAndPhoto(final Long storeId, final String nickname,
		final String profileUrl, final String coverUrl) {
		StoreEntity storeEntity = storeRepository.findById(storeId)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
		storeEntity.setNickname(nickname);
		storeEntity.setRole(Role.STORE);
		storeEntity.setPhoto(profileUrl);
		storeEntity.setCover(coverUrl);
		storeRepository.save(storeEntity);

		long totalStoreCount = storeRepository.count();

		slackWebhookSender.sendSignup("""
			🎉 *새로운 납자기 `%s` 등장!* 🎉
			
			• *누적 가입 회원 수:* %,d명
			• *환경:* `%s`
			""".formatted(
			storeEntity.getNickname(),
			totalStoreCount,
			slackWebhookSender.getCurrentEnvironment()
		));
	}

	@Transactional
	public void updateProfile(final Long storeId, final String cover, final String photo,
		final String nickname, final String description) {
		StoreEntity storeEntity = storeRepository.findById(storeId)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
		storeEntity.setCover(cover);
		storeEntity.setPhoto(photo);
		storeEntity.setNickname(nickname);
		storeEntity.setDescription(description);
	}

	@Transactional
	public void updateRole(final Long storeId, final Role role) {
		StoreEntity storeEntity = storeRepository.findById(storeId)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
		storeEntity.setRole(role);
		storeRepository.save(storeEntity);
	}

	@Transactional
	public void updateWithdraw(final Long storeId) {
		StoreEntity storeEntity = storeRepository.findById(storeId)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
		storeEntity.withdraw();
		storeEntity.clearPhoneVerification();
		storeRepository.save(storeEntity);
	}

	@Transactional
	public void updatePhone(final Long storeId, final String phoneNumber) {
		StoreEntity storeEntity = storeRepository.findById(storeId)
			.orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
		String phoneNumberEnc = phoneEncryptionUtil.encrypt(phoneNumber);
		String phoneNumberHash = phoneEncryptionUtil.hash(phoneNumber);
		storeEntity.verifyAndUpdatePhone(phoneNumberEnc, phoneNumberHash);
		storeRepository.save(storeEntity);
	}
}
