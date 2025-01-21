package com.napzak.domain.store.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.napzak.domain.store.core.entity.GenrePreferenceEntity;

import feign.Param;

public interface GenrePreferenceRepository extends JpaRepository<GenrePreferenceEntity, Long> {

	boolean existsByStoreId(Long storeId);

	List<GenrePreferenceEntity> findByStoreId(Long storeId);

	@Query("SELECT gp.genreId FROM GenrePreferenceEntity gp WHERE gp.storeId = :storeId")
	List<Long> findGenreIdsByStoreId(@Param("storeId") Long storeId);

	@Modifying
	@Query("DELETE FROM GenrePreferenceEntity gp WHERE gp.storeId = :storeId")
	void deleteByStoreId(@Param("storeId") Long storeId);
}
