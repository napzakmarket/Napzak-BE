package com.napzak.domain.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.chat.entity.ChatMessageEntity;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
	Optional<ChatMessageEntity> findById(Long id);

	@Query("SELECT MAX(m.id) FROM ChatMessageEntity m WHERE m.roomId = :roomId")
	Optional<Long> findLastMessageIdByRoomId(@Param("roomId") Long roomId);
}
