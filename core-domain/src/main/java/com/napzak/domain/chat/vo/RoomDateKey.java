package com.napzak.domain.chat.vo;

import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;

@Getter
public class RoomDateKey {
	private final Long roomId;
	private final LocalDate date;

	public RoomDateKey(Long roomId, LocalDate date) {
		if (roomId == null || date == null) {
			throw new IllegalArgumentException("roomId and date can't be null");
		}
		this.roomId = roomId;
		this.date = date;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RoomDateKey)) return false;
		RoomDateKey that = (RoomDateKey) o;
		return Objects.equals(roomId, that.roomId) && Objects.equals(date, that.date);
	}

	@Override
	public int hashCode() {
		return Objects.hash(roomId, date);
	}
}