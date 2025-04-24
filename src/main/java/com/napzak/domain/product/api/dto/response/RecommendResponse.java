package com.napzak.domain.product.api.dto.response;

import java.util.List;

public record RecommendResponse(
	List<RecommendSearchWordDto> searchWordList,
	List<RecommendGenreDto> genreList
) {
}
