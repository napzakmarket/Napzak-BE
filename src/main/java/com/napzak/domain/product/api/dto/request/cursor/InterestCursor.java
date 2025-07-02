package com.napzak.domain.product.api.dto.request.cursor;

import java.time.LocalDateTime;

import com.napzak.global.common.exception.NapzakException;
import com.napzak.global.common.exception.code.ErrorCode;

public class InterestCursor extends Cursor {
	private final Long id;
	private final LocalDateTime createdAt;

	public InterestCursor(Long id, LocalDateTime createdAt) {
		this.id = id;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();

		return stringBuilder
			.append("I-")
			.append(createdAt)
			.append("-")
			.append(id)
			.toString();
	}

	public static InterestCursor fromString(String cursor) {

		System.out.println("Received cursor: " + cursor);

		if (!cursor.startsWith("I-")) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
		String[] split = cursor.split("-");

		if (split.length != 5) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}

		final long id;
		final LocalDateTime createdAt;

		try {
			String createdAtStr = String.join("-", split[1], split[2], split[3]);
			createdAt = LocalDateTime.parse(createdAtStr);
			id = Long.parseLong(split[4]);
		} catch (NumberFormatException e) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
		return new InterestCursor(id, createdAt);
	}

}
