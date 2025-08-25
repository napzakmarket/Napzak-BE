package com.napzak.api.domain.product.dto.request.cursor;

import com.napzak.domain.product.entity.enums.ProductSortOption;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.code.ErrorCode;

public abstract class Cursor {
	public abstract String toString();

	public static Cursor fromString(String cursor, ProductSortOption productSortOption) {
		switch (productSortOption) {
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
