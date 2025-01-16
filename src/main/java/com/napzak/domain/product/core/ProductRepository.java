package com.napzak.domain.product.core;

import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.TradeType;
import feign.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findById(Long productId);

    boolean existsById(Long productId);

    @Query(
            value = """
                    SELECT p.*
                    FROM product p
                    WHERE p.genre_id IN (
                        SELECT gp.genre_id
                        FROM genre_preference gp
                        WHERE gp.store_id = :storeId
                    )
                    AND p.store_id <> :storeId
                    AND p.trade_type = :tradeType
                    ORDER BY p.created_at DESC
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM product p
                    WHERE p.genre_id IN (
                        SELECT gp.genre_id
                        FROM genre_preference gp
                        WHERE gp.store_id = :storeId
                    )
                    AND p.store_id <> :storeId
                    AND p.trade_type = :tradeType
                    """,
            nativeQuery = true
    )
    List<ProductEntity> findRecommendedProductsByStoreIdAndTradeType(
            @Param("storeId") Long storeId,
            @Param("tradeType") String tradeType,
            Pageable pageable
    );


    @Query("""
            SELECT p
            FROM ProductEntity p
            WHERE p.genreEntity.id = :genreId
              AND p.tradeType = :tradeType
              AND p.storeEntity.id <> :storeId
            ORDER BY p.interestCount DESC, p.id DESC
            """)
    List<ProductEntity> findTopSpecificProductByGenreIdAndTradeType(
            Long genreId,
            TradeType tradeType,
            Long storeId,
            Pageable pageable);

    @Query("""
            SELECT p
            FROM ProductEntity p
            WHERE p.tradeType = :tradeType
            AND p.storeEntity.id <> :storeId
            ORDER BY p.interestCount DESC, p.id DESC
            """)
    List<ProductEntity> findTopInterestProductsByStoreIDAndTradeType(
            Long storeId,
            TradeType tradeType,
            Pageable pageable
    );
}
