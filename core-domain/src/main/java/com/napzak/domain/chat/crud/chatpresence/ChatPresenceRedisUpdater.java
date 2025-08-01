package com.napzak.domain.chat.crud.chatpresence;

import org.springframework.stereotype.Component;

import com.napzak.domain.chat.repository.ChatPresenceRedisRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatPresenceRedisUpdater {
	private final ChatPresenceRedisRepository chatPresenceRedisRepository;

	public void joinRoom(Long roomId, Long userId) {
		chatPresenceRedisRepository.joinRoom(roomId, userId);
	}

	public void leaveRoom(Long roomId, Long userId) {
		chatPresenceRedisRepository.leaveRoom(roomId, userId);
	}

	public void removeUserFromAllRooms(Long userId) {
		chatPresenceRedisRepository.removeUserFromAllRooms(userId);
	}
}
