package com.napzak.domain.interest.core;

import com.napzak.domain.interest.core.entity.InterestEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InterestRepository extends JpaRepository<InterestEntity, Long> {

    /*
    @Query(""" 
            SELECT COUNT(i) > 0
            FROM InterestEntity i
            WHERE i.productEntity.id = :productId AND i.storeEntity.id = :storeId
            """)
    boolean existsByProductIdAndStoreId(@Param("productId") Long productId,
                                        @Param("storeId") Long storeId);
     */

    @Query("""
    SELECT i.productEntity.id
    FROM InterestEntity i
    WHERE i.productEntity.id IN :productIds
      AND i.storeEntity.id = :storeId
""")
    List<Long> findLikedProductIdsByProductIdsAndStoreId(
            @Param("productIds") List<Long> productIds,
            @Param("storeId") Long storeId
    );

}