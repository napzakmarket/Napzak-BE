package com.napzak.domain.chat.crud.chatroom;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.repository.ChatRoomRepository;
import com.napzak.domain.chat.vo.ChatRoom;
import com.napzak.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatRoomRetriever {

	private final ChatRoomRepository chatRoomRepository;

	@Transactional(readOnly = true)
	public ChatRoom findById(Long id) {
		return chatRoomRepository.findById(id)
			.map(ChatRoom::fromEntity)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.CHATROOM_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Long findProductIdById(Long roomId) {
		return chatRoomRepository.findProductIdById(roomId)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.CHATROOM_NOT_FOUND));
	}
}
