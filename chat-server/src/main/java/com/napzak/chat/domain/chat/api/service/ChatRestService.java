package com.napzak.chat.domain.chat.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.common.exception.NapzakException;
import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.crud.chatmessage.ChatMessageRetriever;
import com.napzak.domain.chat.crud.chatparticipant.ChatParticipantRetriever;
import com.napzak.domain.chat.crud.chatparticipant.ChatParticipantSaver;
import com.napzak.domain.chat.crud.chatparticipant.ChatParticipantUpdater;
import com.napzak.domain.chat.crud.chatpresence.ChatPresenceRedisRetriever;
import com.napzak.domain.chat.crud.chatroom.ChatRoomRetriever;
import com.napzak.domain.chat.crud.chatroom.ChatRoomSaver;
import com.napzak.domain.chat.crud.chatroom.ChatRoomUpdater;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.chat.vo.ChatParticipant;
import com.napzak.domain.chat.vo.ChatRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRestService {
	private final ChatRoomRetriever chatRoomRetriever;
	private final ChatRoomSaver chatRoomSaver;
	private final ChatRoomUpdater chatRoomUpdater;
	private final ChatMessageRetriever chatMessageRetriever;
	private final ChatParticipantRetriever chatParticipantRetriever;
	private final ChatParticipantUpdater chatParticipantUpdater;
	private final ChatParticipantSaver chatParticipantSaver;
	private final ChatPresenceRedisRetriever chatPresenceRedisRetriever;

	@Transactional
	public Long createChatRoom(Long senderId, Long receiverId, Long productId){
		Optional<Long> existingRoomId = chatParticipantRetriever.findCommonRoomIdByStores(senderId, receiverId);
		if (existingRoomId.isPresent()) {
			throw new NapzakException(ChatErrorCode.CHATROOM_ALREADY_EXISTS);
		}
		ChatRoom savedChatRoom = chatRoomSaver.save(productId);
		chatParticipantSaver.save(savedChatRoom.getId(), senderId);
		chatParticipantSaver.save(savedChatRoom.getId(), receiverId);
		return savedChatRoom.getId();
	}

	@Transactional(readOnly = true)
	public ChatMessagePagination findChatRoomMessages(Long roomId, Long cursorId, int size) {
		List<ChatMessage> messages = chatMessageRetriever.findMessagesByRoomIdAndCursor(roomId, cursorId, size);
		return new ChatMessagePagination(size, messages);
	}

	@Transactional(readOnly = true)
	public Long findOpponentLastReadMessageId(Long roomId, Long myStoreId) {
		return chatParticipantRetriever.findOpponentLastReadMessageId(roomId, myStoreId);
	}

	@Transactional(readOnly = true)
	public Long findOpponentStoreId(Long roomId, Long myStoreId) {
		return chatParticipantRetriever.findOpponentStoreId(roomId, myStoreId);
	}

	@Transactional(readOnly = true)
	public List<ChatParticipant> findMyChatRooms(Long storeId) {
		return chatParticipantRetriever.findAllByStoreId(storeId);
	}

	@Transactional(readOnly = true)
	public List<ChatParticipant> findOpponentsByRoomIds(List<Long> roomIds, Long storeId) {
		return chatParticipantRetriever.findOpponentsByRoomIds(roomIds, storeId);
	}

	@Transactional(readOnly = true)
	public Map<Long, ChatMessage> findLastMessages(List<Long> roomIds) {
		return chatMessageRetriever.findLastMessagesByRoomIds(roomIds);
	}

	@Transactional(readOnly = true)
	public Map<Long, Long> findUnreadCounts(List<ChatParticipant> rooms, Long storeId) {
		try {
			Map<Long, Long> lastReadMap = rooms.stream()
				.collect(Collectors.toMap(
					ChatParticipant::getRoomId,
					p -> Optional.ofNullable(p.getLastReadMessageId()).orElse(0L) // ✅ null-safe 처리
				));
				List<Long> roomIds = new ArrayList<>(lastReadMap.keySet());
			Map<Long, Long> result = chatMessageRetriever.findUnreadCounts(roomIds, storeId, lastReadMap);
			return result;

		} catch (Exception e) {
			log.error("🔥 예외 발생: {}, {}", e.getClass().getName(), e.getMessage(), e);
			throw e;
		}
	}

	@Transactional
	public Long updateProductId(Long roomId, Long newProductId) {
		chatRoomUpdater.updateProductId(roomId, newProductId);
		return newProductId;
	}

	@Transactional(readOnly = true)
	public Long findProductIdByRoomId(Long roomId) {
		return chatRoomRetriever.findProductIdById(roomId);
	}

	@Transactional
	public Set<Long> enterChatRoom(Long roomId, Long storeId){
		boolean isParticipant = chatParticipantRetriever.existsActiveParticipant(roomId, storeId);
		if (!isParticipant) {
			throw new NapzakException(ChatErrorCode.NO_CHATROOM_ACCESS);
		}

		Long messageId = chatMessageRetriever.findLastMessageIdByRoomId(roomId);
		chatParticipantUpdater.updateEnter(roomId, storeId, messageId);

		Set<Long> otherUsers = chatPresenceRedisRetriever.getUsersInRoom(roomId).stream()
			.filter(id -> !id.equals(storeId))
			.collect(Collectors.toSet());

		return otherUsers;
	}

	@Transactional
	public void leaveChatRoom(Long roomId, Long storeId){
		Long messageId = chatMessageRetriever.findLastMessageIdByRoomId(roomId);
		chatParticipantUpdater.updateLeave(roomId, storeId, messageId);
	}

	@Transactional
	public void exitChatRoom(Long roomId, Long storeId){
		chatParticipantUpdater.updateExit(roomId, storeId);
	}

	@Transactional(readOnly = true)
	public List<ChatParticipant> findParticipants(Long roomId) {
		return chatParticipantRetriever.findAllByRoomId(roomId);
	}
}
