package com.napzak.domain.chat.crud.chatpresence;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.napzak.domain.chat.repository.ChatPresenceRedisRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatPresenceRedisRetriever {
	private final ChatPresenceRedisRepository chatPresenceRedisRepository;

	public Set<Long> getUsersInRoom(Long roomId) {
		return chatPresenceRedisRepository.getUsersInRoom(roomId);
	}

	public Set<Long> getUserRooms(Long userId) {
		return chatPresenceRedisRepository.getUserRooms(userId);
	}
}
