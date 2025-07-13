package com.napzak.domain.genre.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.genre.entity.GenreEntity;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long>, GenreRepositoryCustom {
	@Query("SELECT g.name FROM GenreEntity g WHERE g.id = :id")
	String findNameById(@Param("id") Long id);

	@Query("SELECT g.id, g.name FROM GenreEntity g WHERE g.id IN :ids")
	List<Object[]> findNamesByIds(@Param("ids") List<Long> ids);

	@Query("SELECT g FROM GenreEntity g WHERE g.id IN :genreIds")
	List<GenreEntity> findExistingGenreEntityList(@Param("genreIds") List<Long> genreIds);

	@Query("SELECT g FROM GenreEntity g WHERE g.recommendOrder > 0 ORDER BY g.recommendOrder ASC")
	List<GenreEntity> findRecommendGenres();
}
