package com.napzak.chat.domain.chat.api.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.common.exception.NapzakException;
import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.crud.chatmessage.ChatMessageRetriever;
import com.napzak.domain.chat.crud.chatparticipant.ChatParticipantRetriever;
import com.napzak.domain.chat.crud.chatparticipant.ChatParticipantSaver;
import com.napzak.domain.chat.crud.chatparticipant.ChatParticipantUpdater;
import com.napzak.domain.chat.crud.chatroom.ChatRoomRetriever;
import com.napzak.domain.chat.crud.chatroom.ChatRoomSaver;
import com.napzak.domain.chat.crud.chatroom.ChatRoomUpdater;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.chat.vo.ChatParticipant;
import com.napzak.domain.chat.vo.ChatRoom;

import lombok.RequiredArgsConstructor;

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
		Map<Long, Long> lastReadMap = rooms.stream()
			.collect(Collectors.toMap(ChatParticipant::getRoomId, ChatParticipant::getLastReadMessageId));
		List<Long> roomIds = rooms.stream().map(ChatParticipant::getRoomId).toList();
		return chatMessageRetriever.findUnreadCounts(roomIds, storeId, lastReadMap);
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
	public void enterChatRoom(Long roomId, Long storeId){
		boolean isParticipant = chatParticipantRetriever.existsActiveParticipant(roomId, storeId);
		if (!isParticipant) {
			throw new NapzakException(ChatErrorCode.NO_CHATROOM_ACCESS);
		}
		Long messageId = chatMessageRetriever.findLastMessageIdByRoomId(roomId);
		chatParticipantUpdater.updateEnter(roomId, storeId, messageId);
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
