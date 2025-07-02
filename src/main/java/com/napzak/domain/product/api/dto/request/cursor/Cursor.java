package com.napzak.domain.product.api.dto.request.cursor;

import com.napzak.global.common.exception.code.ErrorCode;
import com.napzak.domain.product.api.service.enums.SortOption;
import com.napzak.global.common.exception.NapzakException;

public abstract class Cursor {
	public abstract String toString();

	public static Cursor fromString(String cursor, SortOption sortOption) {
		switch (sortOption) {
			case RECENT:
				return RecentCursor.fromString(cursor);
			case POPULAR:
				return PopularCursor.fromString(cursor);
			case LOW_PRICE:
				return LowPriceCursor.fromString(cursor);
			case HIGH_PRICE:
				return HighPriceCursor.fromString(cursor);
			case INTEREST:
				return InterestCursor.fromString(cursor);
			default:
				throw new NapzakException(ErrorCode.INVALID_SORT_OPTION);
		}
	}
}
