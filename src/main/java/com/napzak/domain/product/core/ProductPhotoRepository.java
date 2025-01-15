package com.napzak.domain.product.core;

import com.napzak.domain.product.core.entity.ProductPhotoEntity;
import com.napzak.domain.product.core.vo.ProductPhoto;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhotoEntity, Long> {

    @Query("""
            SELECT ppe
            FROM ProductPhotoEntity ppe
            WHERE ppe.productEntity.id = :productId
            AND ppe.sequence = 1
            """)
    Optional<ProductPhotoEntity> findFirstSequencePhotoByProductId(@Param("productId") Long productId);
}
