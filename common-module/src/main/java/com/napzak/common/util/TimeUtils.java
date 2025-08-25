package com.napzak.common.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

	public static String calculateChatRoomTime(LocalDateTime createdAt) {
		LocalDateTime now = LocalDateTime.now();

		if (createdAt.toLocalDate().equals(now.toLocalDate())) {
			int hour = createdAt.getHour();
			String meridiem = hour < 12 ? "오전" : "오후";
			int hour12 = hour % 12 == 0 ? 12 : hour % 12;
			String minute = String.format("%02d", createdAt.getMinute());
			return meridiem + " " + hour12 + ":" + minute;
		} else if (createdAt.toLocalDate().equals(now.minusDays(1).toLocalDate())) {
			return "어제";
		} else {
			return createdAt.format(DateTimeFormatter.ofPattern("M월 d일"));
		}
	}

	public static String formatChatMessageTime(LocalDateTime createdAt) {
		int hour = createdAt.getHour();
		String meridiem = hour < 12 ? "오전" : "오후";
		int hour12 = hour % 12 == 0 ? 12 : hour % 12;
		String minute = String.format("%02d", createdAt.getMinute());
		return meridiem + " " + hour12 + ":" + minute;
	}
}
