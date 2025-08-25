package com.napzak.common.textfilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlangFilter {

	private final RedisTemplate<String, String> redisTemplate;

	private static final String RAW_SET = "slang:raw";
	private static final String JAMO_SET = "slang:jamo";
	private static final String MIXED_SET = "slang:mixed";
	private static final int MIN_SLANG_LENGTH = 2;

	public boolean containsSlang(String input) {
		String onlyEng = input.replaceAll("[^a-zA-Z]", "").toLowerCase();
		boolean hasEng = !onlyEng.isEmpty();
		boolean hasKor = input.matches(".*[가-힣ㄱ-ㅎㅏ-ㅣ].*");

		if (hasEng && containsFullSubstring(onlyEng, redisTemplate.opsForSet().members(RAW_SET))) return true;

		String rawJamo = KoreanUtils.extractJamo(input);
		if (containsFullSubstring(rawJamo, redisTemplate.opsForSet().members(JAMO_SET))) return true;

		if (hasKor || (hasEng && input.length() <= 10)) {
			String normalized = normalizeMixed(input);
			String jamo = KoreanUtils.extractJamo(normalized);

			if (containsFullSubstring(jamo, redisTemplate.opsForSet().members(JAMO_SET))) return true;
			if (containsFullSubstring(normalized, redisTemplate.opsForSet().members(MIXED_SET))) return true;
		}

		return false;
	}

	private boolean containsFullSubstring(String input, Set<String> slangSet) {
		if (slangSet == null || slangSet.isEmpty()) return false;
		for (String slang : slangSet) {
			if (slang == null || slang.isBlank() || slang.length() < MIN_SLANG_LENGTH) continue;
			if (input.contains(slang)) return true;
		}
		return false;
	}

	public static String normalizeMixed(String input) {
		StringBuilder sb = new StringBuilder();
		for (char ch : input.toLowerCase().toCharArray()) {
			if (Character.isAlphabetic(ch) || Character.isDigit(ch)) {
				sb.append(MixedNormalizeMap.getOrDefault(ch, ch));
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
}
