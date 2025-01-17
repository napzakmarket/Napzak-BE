package com.napzak.domain.product.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.product.core.entity.ProductPhotoEntity;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhotoEntity, Long> {
	@Query("SELECT p.productEntity.id AS productId, p.photoUrl AS photoUrl " +
		"FROM ProductPhotoEntity p " +
		"WHERE p.sequence = 1 AND p.productEntity.id IN :productIds")
	List<Object[]> findFirstPhotosByProductIds(@Param("productIds") List<Long> productIds);

	@Query("SELECT p FROM ProductPhotoEntity p " +
		"WHERE p.productEntity.id = :productId " +
		"ORDER BY p.sequence ASC")
	List<ProductPhotoEntity> findAllByProductEntityOrderBySequenceAsc(@Param("productId") Long productId);

}
