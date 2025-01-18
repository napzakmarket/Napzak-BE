package com.napzak.domain.product.api.dto.request.cursor;

import com.napzak.global.common.exception.NapzakException;
import com.napzak.global.common.exception.code.ErrorCode;

public class LowPriceCursor extends Cursor {
	private final int price;
	private final long id;

	public LowPriceCursor(int price, long id) {
		this.price = price;
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public long getId() {
		return id;
	}

	// L-{price}-{id}
	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();

		return stringBuilder
			.append("L-")
			.append(price)
			.append("-")
			.append(id)
			.toString();
	}

	public static LowPriceCursor fromString(String cursor) {
		if (!cursor.startsWith("L-")) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
		String[] split = cursor.split("-");

		if (split.length != 3) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}

		final long id;
		final int price;

		try {
			price = Integer.parseInt(split[1]);
			id = Long.parseLong(split[2]);
		} catch (NumberFormatException e) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
		return new LowPriceCursor(price, id);
	}
}
