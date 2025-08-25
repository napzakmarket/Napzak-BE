package com.napzak.api.domain.product.dto.request.cursor;

import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.code.ErrorCode;

public class PopularCursor extends Cursor {
	private final int interestCount;
	private final long id;

	public PopularCursor(int interestCount, long id) {
		this.interestCount = interestCount;
		this.id = id;
	}

	public int getInterestCount() {
		return interestCount;
	}

	public long getId() {
		return id;
	}

	// P-{interestCount}-{id}
	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();

		return stringBuilder
			.append("P-")
			.append(interestCount)
			.append("-")
			.append(id)
			.toString();
	}

	public static PopularCursor fromString(String cursor) {
		if (!cursor.startsWith("P-")) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
		String[] split = cursor.split("-");

		if (split.length != 3) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}

		final int interestCount;
		final long id;

		try {
			interestCount = Integer.parseInt(split[1]);
			id = Long.parseLong(split[2]);
		} catch (NumberFormatException e) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
		return new PopularCursor(interestCount, id);
	}
}
