package com.napzak.api.domain.store.dto.request;

import java.util.List;

public record GenrePreferenceRequest(
	List<Long> genreIds
) {
}
