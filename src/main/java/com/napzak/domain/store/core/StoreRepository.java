package com.napzak.domain.store.core;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.napzak.domain.store.api.dto.response.StoreStatusDto;
import com.napzak.domain.store.core.entity.StoreEntity;
import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.domain.store.core.entity.enums.SocialType;

import feign.Param;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

	@Query("SELECT u FROM StoreEntity u WHERE u.socialId = :socialId AND u.socialType = :socialType")
	Optional<StoreEntity> findBySocialTypeAndSocialId(@Param("socialId") String socialId,
		@Param("socialType") SocialType socialType);

	@Query("""
			SELECT new com.napzak.domain.store.api.dto.response.StoreStatusDto(
			s.id,
			s.photo,
			s.nickname,
			COALESCE(SUM(CASE WHEN p.tradeType = 'SELL' THEN 1 ELSE 0 END), 0),
			COALESCE(SUM(CASE WHEN p.tradeType = 'BUY' THEN 1 ELSE 0 END), 0)
			)
			FROM StoreEntity s
			LEFT JOIN ProductEntity p ON s.id = p.storeId
			WHERE s.id = :storeId
			GROUP BY s.id, s.nickname
		""")
	StoreStatusDto findStoreStatusById(@Param("storeId") Long storeId);

	@Query("SELECT u.nickname FROM StoreEntity u WHERE u.id = :storeId")
	String findNicknameById(@Param("storeId") Long storeId);

	boolean existsByNickname(String nickname);

	@Query("SELECT u.role FROM StoreEntity u WHERE u.id = :storeId")
	Optional<Role> findRoleByStoreId(@Param("storeId") Long storeId);
	@Query("SELECT s.cover FROM StoreEntity s")
	List<String> findAllCover();

	@Query("SELECT s.photo FROM StoreEntity s")
	List<String> findAllPhoto();
}
