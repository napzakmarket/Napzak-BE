package com.napzak.domain.store.repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.napzak.common.util.sms.SmsProperties;
import com.napzak.domain.store.vo.SmsVerificationData;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SmsVerificationRedisRepository {

	private static final String VERIFY_KEY_PREFIX = "sms:verify:";
	private static final String DAILY_KEY_PREFIX = "sms:daily:";

	private final StringRedisTemplate stringRedisTemplate;
	private final ObjectMapper objectMapper;

	private final SmsProperties smsProperties;

	public void saveVerificationCode(String phoneNumber, String code) {
		String key = VERIFY_KEY_PREFIX + phoneNumber;
		String value = serialize(SmsVerificationData.of(code));
		stringRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(smsProperties.getPolicy().getVerifyExpireTime()));
	}

	public Optional<SmsVerificationData> findVerificationData(String phoneNumber) {
		String key = VERIFY_KEY_PREFIX + phoneNumber;
		String value = stringRedisTemplate.opsForValue().get(key);
		if (value == null) {
			return Optional.empty();
		}
		return Optional.of(deserialize(value));
	}

	public void updateVerificationData(String phoneNumber, SmsVerificationData data) {
		String key = VERIFY_KEY_PREFIX + phoneNumber;
		long remainTtl = stringRedisTemplate.getExpire(key);
		stringRedisTemplate.opsForValue().set(key, serialize(data), Duration.ofSeconds(remainTtl));
	}

	public void deleteVerificationData(String phoneNumber) {
		stringRedisTemplate.delete(VERIFY_KEY_PREFIX + phoneNumber);
	}

	public void incrementDailyCount(String phoneNumber) {
		String key = DAILY_KEY_PREFIX + phoneNumber;

		// 키가 존재하지 않을 때 자동적으로 값 0으로 초기화되어 저장되고 +1
		Long count = stringRedisTemplate.opsForValue().increment(key);
		if (count != null && count == 1) {
			stringRedisTemplate.expire(key, remainingSecondsUntilMidnight());
		}
	}

	public int getDailyCount(String phoneNumber) {
		String value = stringRedisTemplate.opsForValue().get(DAILY_KEY_PREFIX + phoneNumber);
		return value == null ? 0 : Integer.parseInt(value);
	}

	private Duration remainingSecondsUntilMidnight() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime midnight = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);
		return Duration.between(now, midnight);
	}

	private String serialize(SmsVerificationData data) {
		try {
			return objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("SmsVerificationData 직렬화 실패", e);
		}
	}

	private SmsVerificationData deserialize(String value) {
		try {
			return objectMapper.readValue(value, SmsVerificationData.class);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("SmsVerificationData 역직렬화 실패", e);
		}
	}
}
