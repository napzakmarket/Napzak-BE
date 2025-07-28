package com.napzak.domain.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.napzak.domain.chat.entity.ChatParticipantEntity;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipantEntity, Long> {
	List<ChatParticipantEntity> findAllByRoomId(Long roomId);

	Optional<ChatParticipantEntity> findByRoomIdAndStoreId(Long roomId, Long storeId);

	@Query("""
        SELECT cp.roomId, cp.lastReadMessageId
        FROM ChatParticipantEntity cp
        WHERE cp.storeId = :storeId AND cp.roomId IN :roomIds
    """)
	List<Object[]> findLastReadMessageIdsByRoomIdsAndStoreId(
		@Param("roomIds") List<Long> roomIds,
		@Param("storeId") Long storeId
	);

	@Query("""
    SELECT cp 
    FROM ChatParticipantEntity cp
    WHERE cp.storeId = :storeId AND cp.isExited = false
""")
	List<ChatParticipantEntity> findAllByStoreId(@Param("storeId") Long storeId);

	@Query("""
    SELECT cp.id
    FROM ChatParticipantEntity cp
    WHERE cp.storeId = :storeId AND cp.isExited = false
""")
	List<Long> findRoomIdsByStoreId(@Param("storeId") Long storeId);

	@Query("""
        SELECT cp
        FROM ChatParticipantEntity cp
        WHERE cp.roomId = :roomId
          AND cp.storeId <> :storeId
    """)
	Optional<ChatParticipantEntity> findOpponentParticipant(
		@Param("roomId") Long roomId,
		@Param("storeId") Long storeId
	);

	// 상대방이 isExited true여도 조회 가능해야하므로 isExited 관련 조건 없음
	@Query("""
    SELECT cp
    FROM ChatParticipantEntity cp
    WHERE cp.roomId IN :roomIds
      AND cp.storeId <> :storeId
""")
	List<ChatParticipantEntity> findOpponentsByRoomIds(
		@Param("roomIds") List<Long> roomIds,
		@Param("storeId") Long storeId
	);

	@Query("""
		SELECT cp1.roomId
		FROM ChatParticipantEntity cp1, ChatParticipantEntity cp2
		WHERE cp1.roomId = cp2.roomId
		  AND cp1.storeId = :storeAId
		  AND cp2.storeId = :storeBId
		  AND cp1.isExited = false
		  AND cp2.isExited = false
		""")
	Optional<Long> findCommonRoomIdByStores(
		@Param("storeAId") Long storeAId,
		@Param("storeBId") Long storeBId
	);

	@Query("SELECT COUNT(cp) > 0 FROM ChatParticipantEntity cp " +
		"WHERE cp.roomId = :roomId AND cp.storeId = :storeId AND cp.isExited = false")
	boolean existsActiveParticipant(
		@Param("roomId") Long roomId,
		@Param("storeId") Long storeId
	);
}
