package com.napzak.domain.product.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.product.core.entity.ProductPhotoEntity;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhotoEntity, Long> {
	@Query("SELECT p.productId AS productId, p.photoUrl AS photoUrl " +
		"FROM ProductPhotoEntity p " +
		"WHERE p.sequence = 1 AND p.productId IN :productIds")
	List<Object[]> findFirstPhotosByProductIds(@Param("productIds") List<Long> productIds);

	@Query("SELECT p.photoUrl " +
		"FROM ProductPhotoEntity p " +
		"WHERE p.sequence = 1 AND p.productId = :productId")
	String findFirstPhotoByProductId(@Param("productId") Long productId);

	@Query("SELECT p FROM ProductPhotoEntity p " +
		"WHERE p.productId = :productId " +
		"ORDER BY p.sequence ASC")
	List<ProductPhotoEntity> findAllByProductEntityOrderBySequenceAsc(@Param("productId") Long productId);

	@Modifying
	@Query("DELETE FROM ProductPhotoEntity  p WHERE p.id IN :ids")
	void deleteAllByProductPhotoIds(@Param("ids") List<Long> ids);

	@Modifying
	@Query("DELETE FROM ProductPhotoEntity p WHERE p.productId = :productId")
	void deleteAllByProductId(@Param("productId") Long productId);

	@Query("SELECT p.photoUrl FROM ProductPhotoEntity p")
	List<String> findAllPhotoUrls();

}
