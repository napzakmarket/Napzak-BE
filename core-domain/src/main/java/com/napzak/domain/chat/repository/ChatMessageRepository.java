package com.napzak.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.chat.entity.ChatMessageEntity;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
	Optional<ChatMessageEntity> findById(Long id);

	@Query("SELECT MAX(m.id) FROM ChatMessageEntity m WHERE m.roomId = :roomId")
	Optional<Long> findLastMessageIdByRoomId(@Param("roomId") Long roomId);

	@Modifying
	@Query(
		value = """
			INSERT INTO chat_message (room_id, type, metadata, created_at)
			VALUES (:roomId, :type, CAST(:metadata AS JSON), NOW(6))
			ON DUPLICATE KEY UPDATE id = id
		""",
		nativeQuery = true
	)
	void insertDateMessageIfNotExists(
		@Param("roomId") Long roomId,
		@Param("type") String type,
		@Param("metadata") String metadata
	);

	@Query(
		value = """
			SELECT *
			FROM chat_message
			WHERE room_id = :roomId
			  AND type = :type
			  AND created_date = CURRENT_DATE()
			LIMIT 1
    	""",
		nativeQuery = true
	)
	Optional<ChatMessageEntity> findTodayDateMessage(
		@Param("roomId") Long roomId,
		@Param("type") String type
	);
}
