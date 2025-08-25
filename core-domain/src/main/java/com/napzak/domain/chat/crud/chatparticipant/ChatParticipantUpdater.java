package com.napzak.domain.chat.crud.chatparticipant;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.entity.ChatParticipantEntity;
import com.napzak.domain.chat.repository.ChatParticipantRepository;
import com.napzak.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatParticipantUpdater {

	private final ChatParticipantRepository chatParticipantRepository;

	@Transactional
	public void updateLastReadMessage(Long roomId, Long storeId, Long messageId){
		ChatParticipantEntity chatParticipant = chatParticipantRepository.findByRoomIdAndStoreId(roomId, storeId)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.PARTICIPANT_NOT_FOUND));
		chatParticipant.updateLastReadMessageId(messageId);
		chatParticipantRepository.save(chatParticipant);
	}

	@Transactional
	public void updateEnter(Long roomId, Long storeId, Long messageId){
		ChatParticipantEntity chatParticipant = chatParticipantRepository.findByRoomIdAndStoreId(roomId, storeId)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.PARTICIPANT_NOT_FOUND));
		chatParticipant.enter(messageId);
		chatParticipantRepository.save(chatParticipant);
	}

	@Transactional
	public void updateLeave(Long roomId, Long storeId, Long messageId){
		ChatParticipantEntity chatParticipant = chatParticipantRepository.findByRoomIdAndStoreId(roomId, storeId)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.PARTICIPANT_NOT_FOUND));
		chatParticipant.leave(messageId);
		chatParticipantRepository.save(chatParticipant);
	}

	@Transactional
	public void updateExit(Long roomId, Long storeId){
		ChatParticipantEntity chatParticipant = chatParticipantRepository.findByRoomIdAndStoreId(roomId, storeId)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.PARTICIPANT_NOT_FOUND));
		chatParticipant.exit();
		chatParticipantRepository.save(chatParticipant);
	}

	@Transactional
	public void exitAllRoomsByStoreId(Long storeId) {
		int exitedRoomsCount = chatParticipantRepository.markAllExitedByStoreId(storeId);
		log.debug("storeId = {}, exitedRoomsCount = {}", storeId, exitedRoomsCount);
	}
}
