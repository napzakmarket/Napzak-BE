package com.napzak.domain.product.api.dto.response;

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
