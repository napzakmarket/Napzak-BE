package com.napzak.domain.push.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.push.entity.PushDeviceTokenEntity;

@Repository
public interface PushDeviceTokenRepository extends JpaRepository<PushDeviceTokenEntity, Long> {

	Optional<PushDeviceTokenEntity> findByStoreIdAndDeviceToken(Long storeId, String deviceToken);

	void deleteByStoreIdAndDeviceToken(Long storeId, String deviceToken);

	@Query("""
        SELECT p.allowMessage
        FROM PushDeviceTokenEntity p
        WHERE p.storeId = :storeId AND p.deviceToken = :deviceToken
    """)
	boolean findAllowMessageByStoreIdAndDeviceToken(
		@Param("storeId") Long storeId,
		@Param("deviceToken") String deviceToken
	);

	// 메세지 푸시 가능 토큰만 조회
	@Query("""
        SELECT p.deviceToken
        FROM PushDeviceTokenEntity p
        WHERE p.storeId = :storeId AND p.isEnabled = true AND p.allowMessage = true
    """)
	List<String> findAllowMessageDeviceTokensByStoreId(@Param("storeId") Long storeId);
}
