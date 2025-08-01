package com.napzak.domain.chat.repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChatPresenceRedisRepository {

	private static final String ROOM_KEY_PREFIX = "chat:room:"; // e.g. chat:room:123
	private static final String USER_KEY_PREFIX = "chat:user:"; // e.g. chat:user:456

	private final RedisTemplate<String, Object> redisTemplate;

	public ChatPresenceRedisRepository(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void joinRoom(Long roomId, Long userId) {
		redisTemplate.opsForSet().add(roomKey(roomId), userId);
		redisTemplate.opsForSet().add(userKey(userId), roomId);
	}

	public void leaveRoom(Long roomId, Long userId) {
		redisTemplate.opsForSet().remove(roomKey(roomId), userId);
		redisTemplate.opsForSet().remove(userKey(userId), roomId);
	}

	public Set<Long> getUsersInRoom(Long roomId) {
		return Optional.ofNullable(redisTemplate.opsForSet().members(roomKey(roomId)))
			.map(this::castSet)
			.orElse(Collections.emptySet());
	}

	public Set<Long> getUserRooms(Long userId) {
		return Optional.ofNullable(redisTemplate.opsForSet().members(userKey(userId)))
			.map(this::castSet)
			.orElse(Collections.emptySet());
	}

	public void removeUserFromAllRooms(Long userId) {
		Set<Long> rooms = getUserRooms(userId);
		for (Long roomId : rooms) {
			leaveRoom(roomId, userId);
		}
	}

	private String roomKey(Long roomId) {
		return ROOM_KEY_PREFIX + roomId;
	}

	private String userKey(Long userId) {
		return USER_KEY_PREFIX + userId;
	}

	@SuppressWarnings("")
	private Set<Long> castSet(Set<?> raw) {
		Set<Long> result = new HashSet<>();
		for (Object item : raw) {
			if (item instanceof Integer i) result.add(i.longValue());
			else if (item instanceof Long l) result.add(l);
			else if (item instanceof String s) result.add(Long.parseLong(s));
		}
		return result;
	}
}
