package com.napzak.api.domain.genre.dto.request.cursor;

import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.code.ErrorCode;

public class OldestCursor {
	private final Long id;

	public OldestCursor(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	// O-{id}
	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder
			.append("O-")
			.append(id)
			.toString();
	}

	public static OldestCursor fromString(String cursor) {
		if (!cursor.startsWith("O-")) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
		String[] split = cursor.split("-");

		if (split.length != 2) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
		final long id;

		try{
			id = Long.parseLong(split[1]);
		} catch (NumberFormatException e) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
		return new OldestCursor(id);
	}
}
