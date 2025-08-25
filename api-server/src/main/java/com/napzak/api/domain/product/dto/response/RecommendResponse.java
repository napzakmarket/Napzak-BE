package com.napzak.api.domain.product.dto.response;

import java.util.List;

public record RecommendResponse(
	List<RecommendSearchWordDto> searchWordList,
	List<RecommendGenreDto> genreList
) {
}
