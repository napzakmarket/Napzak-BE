package com.napzak.domain.store.api.dto;

import java.util.List;

public record GenrePreferenceRequest(
	List<Long> genreIds
) {
}
