package com.napzak.api.domain.product.dto.response;

public record RecommendSearchWordDto(
	long searchWordId,
	String searchWord
) {
	public static RecommendSearchWordDto from(
		long searchWordId,
		String searchWord
	) {
		return new RecommendSearchWordDto(searchWordId, searchWord);
	}
}
