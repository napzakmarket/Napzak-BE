package com.napzak.domain.product.core;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.product.core.entity.ProductEntity;
import com.napzak.domain.product.core.entity.enums.TradeType;

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

	Optional<ProductEntity> findById(Long productId);

	boolean existsById(Long productId);

	/**
	 * 특정 장르에서 storeId가 등록한 상품 제외
	 * tradeStatus = BEFORE_TRADE
	 * tradeType = BUY or SELL
	 * 상위 LIMIT 개 반환
	 */
	@Query(value = """
		SELECT p
		FROM ProductEntity p
		WHERE p.storeId <> :storeId
		  AND p.tradeStatus = 'BEFORE_TRADE'
		  AND p.genreId = :genreId
		  AND p.tradeType = :tradeType
		""")
	List<ProductEntity> findTopByGenreAndTradeTypeExcludingStore(
		@Param("genreId") Long genreId,
		@Param("tradeType") TradeType tradeType,
		@Param("storeId") Long storeId,
		Pageable pageable
	);

	default List<ProductEntity> findTopByGenre(
		Long genreId,
		TradeType tradeType,
		Long storeId,
		int limit,
		Sort sort
	) {
		return findTopByGenreAndTradeTypeExcludingStore(
			genreId,
			tradeType,
			storeId,
			PageRequest.of(0, limit, sort)
		);

	}
}

