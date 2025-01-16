package com.napzak.domain.product.core;

import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.TradeType;
import feign.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("""
            SELECT p
            FROM ProductEntity p
            Where p.genreEntity.id IN (
                SELECT gp.genreEntity.id
                FROM GenrePreferenceEntity gp
                WHERE gp.storeEntity.id = :storeId
            )
            AND p.storeEntity.id <> :storeId
            AND p.tradeType = :tradeType
            ORDER BY p.createdAt DESC
            """)
    List<ProductEntity> findRecommendedProductsByStoreIdAndTradeType(
            Long storeId,
            TradeType tradeType,
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
