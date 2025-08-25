package com.napzak.domain.chat.crud.chatparticipant;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.repository.ChatParticipantRepository;
import com.napzak.domain.chat.vo.ChatParticipant;
import com.napzak.common.exception.NapzakException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatParticipantRetriever {

	private final ChatParticipantRepository chatParticipantRepository;

	@Transactional(readOnly = true)
	public ChatParticipant findByRoomIdAndStoreId(Long roomId, Long storeId) {
		return chatParticipantRepository.findByRoomIdAndStoreId(roomId, storeId)
			.map(ChatParticipant::fromEntity)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.PARTICIPANT_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public List<ChatParticipant> findAllByRoomId(Long roomId) {
		return chatParticipantRepository.findAllByRoomId(roomId).stream()
			.map(ChatParticipant::fromEntity)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<ChatParticipant> findAllByStoreId(Long storeId) {
		return chatParticipantRepository.findAllByStoreId(storeId)
			.stream().map(ChatParticipant::fromEntity)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<ChatParticipant> findOpponentsByRoomIds(List<Long> roomIds, Long storeId) {
		return chatParticipantRepository.findOpponentsByRoomIds(roomIds, storeId)
			.stream().map(ChatParticipant::fromEntity)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<Long> findChatRoomIdsByStoreId(Long storeId) {
		return chatParticipantRepository.findRoomIdsByStoreId(storeId);
	}

	@Transactional(readOnly = true)
	public Long findOpponentLastReadMessageId(Long roomId, Long myStoreId) {
		return chatParticipantRepository.findOpponentParticipant(roomId, myStoreId)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.PARTICIPANT_NOT_FOUND))
			.getLastReadMessageId();
	}

	@Transactional(readOnly = true)
	public Long findOpponentStoreId(Long roomId, Long myStoreId) {
		return chatParticipantRepository.findOpponentParticipant(roomId, myStoreId)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.PARTICIPANT_NOT_FOUND))
			.getStoreId();
	}

	@Transactional(readOnly = true)
	public Optional<Long> findCommonRoomIdByStores(Long myStoreId, Long opponentStoreId) {
		return chatParticipantRepository.findCommonRoomIdByStores(myStoreId, opponentStoreId);
	}

	@Transactional(readOnly = true)
	public boolean existsActiveParticipant(Long roomId, Long storeId) {
		return chatParticipantRepository.existsActiveParticipant(roomId, storeId);
	}
}
