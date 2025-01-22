package com.napzak.domain.store.api.dto.request;

import java.util.List;

public record GenrePreferenceRequest(
	List<Long> genreIds
) {
}
