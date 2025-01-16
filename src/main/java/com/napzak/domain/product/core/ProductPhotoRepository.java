package com.napzak.domain.product.core;

import com.napzak.domain.product.api.dto.ProductPhotoDto;
import com.napzak.domain.product.core.entity.ProductPhotoEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhotoEntity, Long> {

    /*
    @Query("""
            SELECT ppe
            FROM ProductPhotoEntity ppe
            WHERE ppe.productEntity.id = :productId
            AND ppe.sequence = 1
            """)
    Optional<ProductPhotoEntity> findFirstSequencePhotoByProductId(@Param("productId") Long productId);
    */

    @Query("""
        SELECT new com.napzak.domain.product.api.dto.ProductPhotoDto(
                ppe.productEntity.id,
                ppe.photoUrl
                )
        FROM ProductPhotoEntity ppe
        WHERE ppe.sequence = 1
          AND ppe.productEntity.id IN :productIds
        """)
    List<ProductPhotoDto> findFirstSequencePhotosByProductIds(
            @Param("productIds") List<Long> productIds
    );
}

