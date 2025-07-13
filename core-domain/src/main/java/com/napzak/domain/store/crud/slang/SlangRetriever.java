package com.napzak.domain.store.crud.slang;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.napzak.domain.store.entity.SlangEntity;
import com.napzak.domain.store.repository.SlangRepository;
import com.napzak.common.textfilter.KoreanUtils;
import com.napzak.common.textfilter.SlangFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlangRetriever {

	private final SlangRepository slangRepository;
	private final RedisTemplate<String, String> redisTemplate;

	private static final String RAW_SET = "slang:raw";
	private static final String JAMO_SET = "slang:jamo";
	private static final String MIXED_SET = "slang:mixed";

	@EventListener(ApplicationReadyEvent.class)
	public void preloadSlang() {
		updateSlangToRedis();
	}

	public void updateSlangToRedis() {
		log.info("🔥 updateSlangToRedis start");
		List<SlangEntity> slangs = slangRepository.findAll();
		log.info("🧠 slang rows: {}", slangs.size());

		Set<String> rawWords = slangs.stream()
			.map(SlangEntity::getWord)
			.filter(w -> w.matches("[a-zA-Z]+"))
			.collect(Collectors.toSet());

		Set<String> jamoWords = slangs.stream()
			.map(SlangEntity::getWord)
			.map(KoreanUtils::extractJamo)
			.filter(jamo -> jamo != null && !jamo.isBlank() && jamo.length() >= 3)
			.collect(Collectors.toSet());

		Set<String> mixedWords = slangs.stream()
			.map(SlangEntity::getWord)
			.map(KoreanUtils::extractJamo)
			.map(SlangFilter::normalizeMixed)
			.collect(Collectors.toSet());

		redisTemplate.delete(RAW_SET);
		redisTemplate.delete(JAMO_SET);
		redisTemplate.delete(MIXED_SET);

		redisTemplate.opsForSet().add(RAW_SET, rawWords.toArray(new String[0]));
		redisTemplate.opsForSet().add(JAMO_SET, jamoWords.toArray(new String[0]));
		redisTemplate.opsForSet().add(MIXED_SET, mixedWords.toArray(new String[0]));
	}
}
