package com.napzak.domain.chat.crud.chatmessage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.repository.ChatMessageRepository;
import com.napzak.domain.chat.repository.ChatMessageRepositoryCustom;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatMessageRetriever {

	private final ChatMessageRepository chatMessageRepository;
	private final ChatMessageRepositoryCustom chatMessageRepositoryCustom;

	@Transactional(readOnly = true)
	public ChatMessage findById(Long id) {
		return chatMessageRepository.findById(id)
			.map(ChatMessage::fromEntity)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.MESSAGE_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public List<ChatMessage> findMessagesByRoomIdAndCursor(Long roomId, Long cursorId, int size) {
		return chatMessageRepositoryCustom.findByRoomIdAndCursor(roomId, cursorId, size)
			.stream().map(ChatMessage::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public Long findLastMessageIdByRoomId(Long roomId) {
		return chatMessageRepository.findLastMessageIdByRoomId(roomId).orElse(null);
	}

	@Transactional(readOnly = true)
	public Map<Long, Long> findUnreadCounts(List<Long> roomIds, Long storeId, Map<Long, Long> lastReadMap) {
		return chatMessageRepositoryCustom.findUnreadCountsByRoomIdsAndStoreId(roomIds, storeId, lastReadMap);
	}

	@Transactional(readOnly = true)
	public Map<Long, ChatMessage> findLastMessagesByRoomIds(List<Long> roomIds) {
		Map<Long, ChatMessage> lastMessages = chatMessageRepositoryCustom.findLastMessagesByRoomIds(roomIds)
			.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> ChatMessage.fromEntity(e.getValue())
			));

		for (Long roomId : roomIds) {
			lastMessages.putIfAbsent(roomId, ChatMessage.empty(roomId));
		}

		return lastMessages;

	}

	@Transactional(readOnly = true)
	public boolean existsDateMessageToday(Long roomId, LocalDate today){
		return chatMessageRepositoryCustom.existsDateMessageToday(roomId, today);
	}
}
