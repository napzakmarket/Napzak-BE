package com.napzak.common.textfilter;

public class KoreanUtils {

	private static final char[] INITIAL_JAMOS = {
		'ㄱ','ㄲ','ㄴ','ㄷ','ㄸ','ㄹ','ㅁ','ㅂ','ㅃ','ㅅ','ㅆ','ㅇ','ㅈ','ㅉ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ'
	};

	private static final char[] MEDIAL_JAMOS = {
		'ㅏ','ㅐ','ㅑ','ㅒ','ㅓ','ㅔ','ㅕ','ㅖ','ㅗ','ㅘ','ㅙ','ㅚ',
		'ㅛ','ㅜ','ㅝ','ㅞ','ㅟ','ㅠ','ㅡ','ㅢ','ㅣ'
	};

	private static final char[] FINAL_JAMOS = {
		'\0','ㄱ','ㄲ','ㄳ','ㄴ','ㄵ','ㄶ','ㄷ','ㄹ','ㄺ','ㄻ','ㄼ','ㄽ','ㄾ','ㄿ','ㅀ','ㅁ','ㅂ',
		'ㅄ','ㅅ','ㅆ','ㅇ','ㅈ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ'
	};

	public static String extractJamo(String input) {
		StringBuilder sb = new StringBuilder();
		for (char ch : input.toCharArray()) {
			if (ch >= 0xAC00 && ch <= 0xD7A3) {
				int base = ch - 0xAC00;
				int initialIndex = base / (21 * 28);
				int medialIndex = (base % (21 * 28)) / 28;
				int finalIndex = base % 28;

				sb.append(INITIAL_JAMOS[initialIndex]);
				sb.append(MEDIAL_JAMOS[medialIndex]);
				if (finalIndex != 0) sb.append(FINAL_JAMOS[finalIndex]);
			} else if ((ch >= 0x3131 && ch <= 0x318E)) {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
}
