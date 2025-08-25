package com.napzak.chat.domain.chat.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.napzak.domain.chat.crud.chatpresence.ChatPresenceRedisRetriever;
import com.napzak.domain.chat.entity.enums.MessageType;
import com.napzak.domain.push.util.FcmPushSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatPushService {
	private final ChatPresenceRedisRetriever chatPresenceRedisRetriever;
	private final FcmPushSender fcmPushSender;

	/** presence 조회 + 판단 + 전송을 한 곳에서 응집 */
	public void sendPushIfEligible(Long roomId,
		MessageType type,
		String content,
		List<String> deviceTokens,
		String opponentNickname,
		Long opponentId) {
		Set<Long> occupantIds = new HashSet<>(chatPresenceRedisRetriever.getUsersInRoom(roomId));
		boolean eligible = shouldSendPush(occupantIds, opponentId, opponentNickname, deviceTokens);
		if (eligible) {
			log.info("📱 Sending push to opponent: {}", opponentId);
			sendChatPush(deviceTokens, opponentNickname, type, roomId, content, opponentId);
		} else {
			// 간단 사유 로깅
			boolean hasNickname = opponentNickname != null && !opponentNickname.isBlank();
			boolean hasTokens = deviceTokens != null && !deviceTokens.isEmpty();
			boolean hasOpponentId = opponentId != null;
			boolean opponentOnline = hasOpponentId && occupantIds.contains(opponentId);
			log.debug("Skip push: hasNickname={}, hasTokens={}, hasOpponentId={}, opponentOnline={}",
				hasNickname, hasTokens, hasOpponentId, opponentOnline);
		}
	}

	private void sendChatPush(List<String> deviceTokens, String nickname, MessageType type, Long roomId, String content, Long opponentId) {
		String body = switch (type) {
			case TEXT -> content;
			case IMAGE -> "(사진)";
			default -> null;
		};
		if (body == null) return; // 미지원 타입은 푸시 생략

		for (String token : deviceTokens) {
			try {
				fcmPushSender.sendMessage(
					opponentId,
					token,
					nickname,
					body,
					Map.of("type", "chat", "roomId", String.valueOf(roomId))
				);
			} catch (Exception e) {
				log.warn("FCM send failed for roomId={}, opponentId={}: {}", roomId, opponentId, e.toString());
			}
		}
	}

	/** 순수 함수로 판단만 수행 */
	private boolean shouldSendPush(Set<Long> occupantIds,
		Long opponentId,
		String opponentNickname,
		List<String> deviceTokens) {
		if (opponentNickname == null || opponentNickname.isBlank()) return false;
		if (deviceTokens == null || deviceTokens.isEmpty()) return false;
		if (opponentId == null) return false;
		return !occupantIds.contains(opponentId);
	}
}
