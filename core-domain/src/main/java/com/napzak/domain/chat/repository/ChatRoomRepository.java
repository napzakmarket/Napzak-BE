package com.napzak.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.chat.entity.ChatRoomEntity;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
	Optional<ChatRoomEntity> findById(Long id);

	@Query("SELECT r.productId FROM ChatRoomEntity r WHERE r.id = :id")
	Optional<Long> findProductIdById(@Param("id") Long id);
}
