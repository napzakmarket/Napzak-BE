package com.napzak.domain.chat.crud.chatroom;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.chat.entity.ChatRoomEntity;
import com.napzak.domain.chat.repository.ChatRoomRepository;
import com.napzak.domain.chat.vo.ChatRoom;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatRoomSaver {

	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public ChatRoom save(Long productId){
		ChatRoomEntity entity = ChatRoomEntity.create(productId);
		chatRoomRepository.save(entity);
		return ChatRoom.fromEntity(entity);
	}
}
