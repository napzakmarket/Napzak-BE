package com.napzak.domain.interest.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.interest.core.entity.InterestEntity;

@Repository
public interface InterestRepository extends JpaRepository<InterestEntity, Long> {
	@Query("SELECT COUNT(i) > 0 " +
		"FROM InterestEntity i " +
		"WHERE i.productId = :productId AND i.storeId = :storeId")
	boolean existsByProductIdAndStoreId(@Param("productId") Long productId,
		@Param("storeId") Long storeId);

	@Query("SELECT i.productId " +
		"FROM InterestEntity i " +
		"WHERE i.productId IN :productIds AND i.storeId = :storeId")
	List<Long> findLikedProductIdsByStore(@Param("productIds") List<Long> productIds,
		@Param("storeId") Long storeId);

	@Modifying
	@Query("""
                DELETE FROM InterestEntity i
                WHERE i.storeId = :storeId
                AND i.productId = :productId
            """)
	void deleteByProductIdAndStoreId(
		@Param("productId") Long productId,
		@Param("storeId") Long storeId);

	@Modifying
	@Query("""
				DELETE FROM InterestEntity i
				WHERE i.productId = :productId
			""")
	void deleteByProductId(@Param("productId") Long productId);

	@Modifying
	@Query("""
				DELETE FROM InterestEntity i
				WHERE i.storeId = :storeId
			""")
	void deleteByStoreId(@Param("storeId") Long storeId);

	@Query("""
				SELECT i FROM InterestEntity i
				WHERE i.storeId = :storeId
				AND i.productId = :productId
			""")
	InterestEntity findByProductIdAndStoreId(Long productId, Long storeId);
}
