package com.napzak.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.napzak.domain.store.entity.StoreBlockEntity;

public interface StoreBlockRepository extends JpaRepository<StoreBlockEntity, Long> {

	// 내가 상대방을 차단했는지 조회
	boolean existsByBlockerStoreIdAndBlockedStoreId(Long blockerStoreId, Long blockedStoreId);

	// 나/상대 중 한 명이라도 서로 차단했는지 조회 (양방향 OR)
	@Query("""
    SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
    FROM StoreBlockEntity b
    WHERE (b.blockerStoreId = :myStoreId AND b.blockedStoreId = :otherStoreId)
       OR (b.blockerStoreId = :otherStoreId AND b.blockedStoreId = :myStoreId)
    """)
	boolean existsBlockBetween(@Param("myStoreId") Long myStoreId,
		@Param("otherStoreId") Long otherStoreId);

	// 차단 해제용 — 내 → 상대 차단 레코드 삭제
	long deleteByBlockerStoreIdAndBlockedStoreId(Long blockerStoreId, Long blockedStoreId);
}
