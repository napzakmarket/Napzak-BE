package com.napzak.domain.chat.crud.chatroom;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.common.exception.NapzakException;
import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.entity.ChatRoomEntity;
import com.napzak.domain.chat.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatRoomUpdater {

	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public void updateProductId(Long roomId, Long productId) {
		ChatRoomEntity room = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.CHATROOM_NOT_FOUND));
		room.updateProductId(productId);
	}
}
