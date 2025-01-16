package com.napzak.domain.interest.core;

import com.napzak.domain.interest.core.entity.InterestEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InterestRepository extends JpaRepository<InterestEntity, Long> {


    @Query(""" 
            SELECT COUNT(i) > 0
            FROM InterestEntity i
            WHERE i.productId = :productId AND i.storeId = :storeId
            """)
    boolean existsByProductIdAndStoreId(@Param("productId") Long productId,
                                        @Param("storeId") Long storeId);


    @Query("""
    SELECT i.productId
    FROM InterestEntity i
    WHERE i.productId IN :productIds
      AND i.storeId = :storeId
""")
    List<Long> findLikedProductIdsByProductIdsAndStoreId(
            @Param("productIds") List<Long> productIds,
            @Param("storeId") Long storeId
    );

}