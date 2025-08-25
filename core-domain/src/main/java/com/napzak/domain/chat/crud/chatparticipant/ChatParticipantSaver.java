package com.napzak.domain.chat.crud.chatparticipant;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.chat.entity.ChatParticipantEntity;
import com.napzak.domain.chat.repository.ChatParticipantRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatParticipantSaver {

	private final ChatParticipantRepository chatParticipantRepository;

	@Transactional
	public void save(Long roomId, Long storeId) {
		ChatParticipantEntity entity = ChatParticipantEntity.create(roomId, storeId);
		chatParticipantRepository.save(entity);
	}
}
