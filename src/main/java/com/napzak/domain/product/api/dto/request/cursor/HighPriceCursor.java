package com.napzak.domain.product.api.dto.request.cursor;

import com.napzak.domain.product.api.exception.ProductErrorCode;
import com.napzak.global.common.exception.NapzakException;

public class HighPriceCursor extends Cursor{
	private final int price;
	private final long id;

	public HighPriceCursor(int price, long id){
		this.price = price;
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public long getId() {
		return id;
	}

	// H-{price}-{id}
	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();

		return stringBuilder
			.append("H-")
			.append(price)
			.append("-")
			.append(id)
			.toString();
	}

	public static HighPriceCursor fromString(String cursor){
		if (!cursor.startsWith("H-")) {
			throw new NapzakException(ProductErrorCode.INVALID_CURSOR_FORMAT);
		}
		String[] split = cursor.split("-");

		if (split.length != 3) {
			throw new NapzakException(ProductErrorCode.INVALID_CURSOR_FORMAT);
		}

		final long id;
		final int price;

		try{
			price = Integer.parseInt(split[1]);
			id = Long.parseLong(split[2]);
		} catch (NumberFormatException e) {
			throw new NapzakException(ProductErrorCode.INVALID_CURSOR_FORMAT);
		}
		return new HighPriceCursor(price, id);
	}
}
