package com.napzak.api.admin;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.chat.crud.chatparticipant.ChatParticipantRetriever;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminChatFacade {

	private final ChatParticipantRetriever chatParticipantRetriever;

	@Transactional(readOnly = true)
	public List<Long> getChatRoomIds(Long storeId) {
		return chatParticipantRetriever.findChatRoomIdsByStoreId(storeId);
	}
}
