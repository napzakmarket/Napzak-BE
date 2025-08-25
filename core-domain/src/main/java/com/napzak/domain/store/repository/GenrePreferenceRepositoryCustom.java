package com.napzak.domain.store.repository;

import java.util.List;

public interface GenrePreferenceRepositoryCustom {
	void bulkInsert(Long storeId, List<Long> genreIds);
}