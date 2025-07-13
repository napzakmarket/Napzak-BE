package com.napzak.api.domain.store;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.chat.crud.chatmessage.ChatMessageSaver;
import com.napzak.domain.chat.crud.chatparticipant.ChatParticipantRetriever;
import com.napzak.domain.chat.entity.enums.SystemMessageType;
import com.napzak.domain.chat.vo.ChatMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreChatFacade {

	private final ChatParticipantRetriever chatParticipantRetriever;
	private final ChatMessageSaver chatMessageSaver;

	@Transactional(readOnly = true)
	public List<Long> getChatRoomIds(Long storeId) {
		return chatParticipantRetriever.findChatRoomIdsByStoreId(storeId);
	}

	@Transactional
	public List<ChatMessage> broadcastSystemMessage(Long storeId, SystemMessageType systemMessageType) {
		List<Long> roomIds = getChatRoomIds(storeId);
		return chatMessageSaver.broadcastSystemMessage(roomIds, systemMessageType);
	}
}
