package com.napzak.global.common.slang;

import java.util.Map;

public class MixedNormalizeMap {
	private static final Map<Character, Character> map = Map.ofEntries(
		Map.entry('1', 'ㅣ'), Map.entry('i', 'ㅣ'), Map.entry('l', 'ㅣ'), Map.entry('|', 'ㅣ'),
		Map.entry('s', 'ㅅ'), Map.entry('$', 'ㅅ'),
		Map.entry('b', 'ㅂ'), Map.entry('a', 'ㅏ'), Map.entry('o', 'ㅗ'),
		Map.entry('0', 'ㅇ'), Map.entry('c', 'ㅋ'), Map.entry('k', 'ㅋ')
	);

	public static char getOrDefault(char key, char defaultValue) {
		return map.getOrDefault(key, defaultValue);
	}
}