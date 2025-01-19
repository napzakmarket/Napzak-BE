package com.napzak.domain.product.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.product.core.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
    @Modifying
    @Query("""
            UPDATE ProductEntity p
            SET p.interestCount = p.interestCount + 1 
            WHERE p.id = :productId
            """)
    void incrementInterestCount(@Param("productId") Long productId);

    @Modifying
    @Query("""
            UPDATE ProductEntity p
            SET p.interestCount = p.interestCount - 1
            WHERE p.id = :productId
            AND p.interestCount > 0""")
    void decrementInterestCount(@Param("productId") Long productId);
}
