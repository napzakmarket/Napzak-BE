package com.napzak.domain.product.api.dto.request.cursor;

import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.global.common.exception.NapzakException;

public class RecentCursor extends Cursor {
	private final Long id;

	public RecentCursor(Long id) {
		this.id = id;
	}

	public Long getid() {
		return id;
	}

	// R-{id}
	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder
			.append("R-")
			.append(id)
			.toString();
	}

	public static RecentCursor fromString(String cursor) {
		if (!cursor.startsWith("R-")) {
			throw new NapzakException(ProductErrorCode.INVALID_CURSOR_FORMAT);
		}
		String[] split = cursor.split("-");

		if (split.length != 2) {
			throw new NapzakException(ProductErrorCode.INVALID_CURSOR_FORMAT);
		}
		final long id;

		try{
			id = Long.parseLong(split[1]);
		} catch (NumberFormatException e) {
			throw new NapzakException(ProductErrorCode.INVALID_CURSOR_FORMAT);
		}
		return new RecentCursor(id);
	}
}
