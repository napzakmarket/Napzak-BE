package com.napzak.domain.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.product.entity.ProductEntity;
import com.napzak.domain.product.entity.enums.TradeStatus;
import com.napzak.domain.product.entity.enums.TradeType;

import jakarta.persistence.LockModeType;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM ProductEntity p where p.id = :productId")
	Optional<ProductEntity> lockById(@Param("productId") Long productId);

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
		AND p.interestCount > 0
		""")
	void decrementInterestCount(@Param("productId") Long productId);

	@Modifying
	@Query("""
		UPDATE ProductEntity p
		SET p.tradeStatus = :tradeStatus
		WHERE p.id = :productId
		""")
	void updateTradeStatus(@Param("productId") Long productId, @Param("tradeStatus") TradeStatus tradeStatus);

	@Modifying
	@Query("""
		UPDATE ProductEntity p
		SET p.isVisible = false
		WHERE p.storeId = :storeId
		""")
	void updateIsVisible(@Param("storeId") Long storeId);

	List<ProductEntity> findByIdInAndTradeType(List<Long> ids, TradeType tradeType);

	@Query("SELECT p FROM ProductEntity p where p.id = :productId AND p.isVisible = true")
	Optional<ProductEntity> findById(@Param("productId") Long productId);

	@Query("SELECT p FROM ProductEntity p where p.id = :productId")
	Optional<ProductEntity> findByIdIncludingInvisible(@Param("productId") Long productId);

	int countByStoreIdAndTradeTypeAndIsVisibleTrue(Long storeId, TradeType tradeType);
}

