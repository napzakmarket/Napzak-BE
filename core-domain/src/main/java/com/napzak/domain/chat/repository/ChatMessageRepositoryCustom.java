package com.napzak.domain.chat.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.napzak.domain.chat.entity.ChatMessageEntity;

public interface ChatMessageRepositoryCustom {
	Map<Long, Long> findUnreadCountsByRoomIdsAndStoreId(List<Long> roomIds, Long storeId, Map<Long, Long> lastReadMap);

	Map<Long, ChatMessageEntity> findLastMessagesByRoomIds(List<Long> roomIds);

	List<ChatMessageEntity> findByRoomIdAndCursor(Long roomId, Long cursorId, int size);

	boolean existsDateMessageToday(Long roomId, LocalDate today);
}
