package com.napzak.api.domain.store.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import com.napzak.api.domain.store.dto.request.SmsConfirmRequest;
import com.napzak.api.domain.store.dto.request.SmsSendRequest;
import com.napzak.api.domain.store.dto.response.SmsConfirmResponse;
import com.napzak.api.domain.store.dto.response.SmsSendResponse;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.util.encryption.PhoneEncryptionUtil;
import com.napzak.common.util.sms.SmsProperties;
import com.napzak.common.util.sms.SmsUtil;
import com.napzak.domain.store.code.SmsErrorCode;
import com.napzak.domain.store.code.StoreErrorCode;
import com.napzak.domain.store.crud.store.StoreUpdater;
import com.napzak.domain.store.entity.StoreEntity;
import com.napzak.domain.store.repository.SmsVerificationRedisRepository;
import com.napzak.domain.store.repository.StoreRepository;
import com.napzak.domain.store.vo.SmsVerificationData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

	private final SmsUtil smsUtil;
	private final SmsProperties smsProperties;
	private final PhoneEncryptionUtil phoneEncryptionUtil;

	private final SmsVerificationRedisRepository smsVerificationRedisRepository;
	private final StoreRepository storeRepository;

	private final DefaultMessageService messageService;
	private final StoreService storeService;
	private final StoreUpdater storeUpdater;

	public SmsSendResponse sendVerificationCode(SmsSendRequest request, Long storeId) {
		String phoneNumber = request.phoneNumber();

		// 유효성 검사
		int remainingCount = validateDailyLimit(phoneNumber);
		validatePhoneNumberUsage(phoneNumber, storeId);

		String authCode = SmsUtil.generateVerificationCode();

		// 로그 출력을 위해 마지막 2개 숫자 제외 전화번호 마스킹
		String masked = phoneNumber.replaceAll("(\\d{3})-?(\\d{3,4})-?(\\d{2})(\\d{2})", "$1-****-**$4");

		Message message = new Message();
		message.setFrom(smsProperties.getCoolsms().getSenderNumber());
		message.setTo(phoneNumber);
		message.setText(smsUtil.formatMessageText(authCode));

		try {
			messageService.sendOne(new SingleMessageSendingRequest(message));
			log.info("[SMS] 인증번호가 발송되었습니다. 발송 대상 번호: {}", masked);
		} catch (Exception e) {
			log.error("[SMS] 인증번호 발송이 실패하였습니다. 발송 대상 번호: {}, error: {}", masked, e.getMessage());
			throw new NapzakException(SmsErrorCode.MESSAGE_SEND_FAILED);
		}

		// 레디스에 세션 저장 & 업데이트
		smsVerificationRedisRepository.saveVerificationCode(phoneNumber, authCode);
		smsVerificationRedisRepository.incrementDailyCount(phoneNumber);

		return new SmsSendResponse(remainingCount - 1);
	}

	public SmsConfirmResponse confirmVerificationCode(@Valid SmsConfirmRequest request, Long storeId) {
		String phoneNumber = request.phoneNumber();

		int remainingCount = validateDailyLimit(phoneNumber);

		// 인증번호 요청과 검증 사이에 같은 번호가 가입되었는지 검증
		validatePhoneNumberUsage(phoneNumber, storeId);

		Optional<SmsVerificationData> verificationData = smsVerificationRedisRepository.findVerificationData(phoneNumber);

		if (verificationData.isEmpty()) {
			throw new NapzakException(SmsErrorCode.VERIFICATION_SESSION_NOT_FOUND);
		}

		if (verificationData.get().failCount() >= smsProperties.getPolicy().getFailMaxCount()) {
			throw new NapzakException(SmsErrorCode.VERIFICATION_FAIL_COUNT_EXCEEDED);
		}

		boolean isCodeMatching = request.code().equals(verificationData.get().code());

		try {
			if (isCodeMatching) {
				storeUpdater.updatePhone(storeId, phoneNumber);
				smsVerificationRedisRepository.deleteVerificationData(phoneNumber);
			}
			else {
				// failCount를 증가시켜 세션 업데이트
				SmsVerificationData updated = verificationData.get().incrementFailCount();
				smsVerificationRedisRepository.updateVerificationData(phoneNumber, updated);
			}

			boolean isPhoneVerified = storeService.getStore(storeId).isPhoneVerified();

			return new SmsConfirmResponse(isPhoneVerified, remainingCount);
		} catch (Exception e) {
			throw new NapzakException(SmsErrorCode.MESSAGE_CONFIRM_FAILED);
		}
	}

	private int validateDailyLimit(String phoneNumber) {
		int remainingCount = smsProperties.getPolicy().getSendMaxCount() - smsVerificationRedisRepository.getDailyCount(phoneNumber);
		if (remainingCount == 0) {
			throw new NapzakException(SmsErrorCode.DAILY_SEND_LIMIT_EXCEEDED);
		}
		return remainingCount;
	}

	private void validatePhoneNumberUsage(String phoneNumber, Long storeId) {
		String phoneNumberHash = phoneEncryptionUtil.hash(phoneNumber);
		Optional<StoreEntity> store = storeRepository.findByPhoneNumberHash(phoneNumberHash);
		if (store.isEmpty()) {
			return;
		}
		if (store.get().getId().equals(storeId)) {
			throw new NapzakException(StoreErrorCode.ALREADY_VERIFIED);
		}
		throw new NapzakException(StoreErrorCode.PHONE_NUMBER_ALREADY_IN_USE);
	}
}
