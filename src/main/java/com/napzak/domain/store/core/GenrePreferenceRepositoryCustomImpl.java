package com.napzak.domain.store.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class GenrePreferenceRepositoryCustomImpl implements GenrePreferenceRepositoryCustom {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	@Override
	public void bulkInsert(Long storeId, List<Long> genreIds) {
		String sql = "INSERT INTO genre_preference (store_id, genre_id) VALUES (?, ?)";
		List<Object[]> batchArgs = genreIds.stream()
			.map(genreId -> new Object[] {storeId, genreId})
			.toList();

		jdbcTemplate.batchUpdate(sql, batchArgs);
	}
}
