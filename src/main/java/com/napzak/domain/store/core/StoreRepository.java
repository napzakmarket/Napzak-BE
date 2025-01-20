package com.napzak.domain.store.core;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.napzak.domain.store.api.dto.StoreStatusDto;
import com.napzak.domain.store.core.entity.StoreEntity;
import com.napzak.domain.store.core.entity.enums.SocialType;

import feign.Param;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

	@Query("SELECT u FROM StoreEntity u WHERE u.socialId = :socialId AND u.socialType = :socialType")
	Optional<StoreEntity> findBySocialTypeAndSocialId(@Param("socialId") Long socialId,
		@Param("socialType") SocialType socialType);

	@Query("""
		    SELECT new com.napzak.domain.store.api.dto.StoreStatusDto(
		        s.id,
		        s.photo,
		        s.nickname,
		        COUNT(p.id),
		        SUM(CASE WHEN p.tradeStatus = 'COMPLETED' THEN 1 ELSE 0 END)
		    )
		    FROM StoreEntity s
		    LEFT JOIN ProductEntity p ON s.id = p.storeId
		    WHERE s.id = :storeId
		    GROUP BY s.id, s.nickname
		""")
	StoreStatusDto findStoreStatusById(@Param("storeId") Long storeId);

}
