package com.napzak.global.common.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtils {

	private TimeUtils() {
		// Utility 클래스는 인스턴스화 방지
	}

	public static String calculateUploadTime(LocalDateTime createdAt) {
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(createdAt, now);

		if (duration.toMinutes() < 1) {
			return "방금";
		} else if (duration.toMinutes() < 60) {
			return duration.toMinutes() + "분 전";
		} else if (duration.toHours() < 24) {
			return duration.toHours() + "시간 전";
		} else if (duration.toDays() < 31) {
			return duration.toDays() + "일 전";
		} else if (duration.toDays() < 365) {
			return (duration.toDays() / 30) + "개월 전";
		} else {
			return (duration.toDays() / 365) + "년 전";
		}
	}
}
