package com.napzak.chat.domain.chat.api.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.napzak.chat.domain.chat.api.ChatPushFacade;
import com.napzak.chat.domain.chat.api.ChatStoreFacade;
import com.napzak.chat.domain.chat.api.dto.request.ChatMessageRequest;
import com.napzak.chat.domain.chat.api.service.ChatRestService;
import com.napzak.chat.domain.chat.api.service.ChatWebSocketService;
import com.napzak.common.auth.context.StoreSession;
import com.napzak.domain.chat.entity.enums.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

	private final ChatWebSocketService chatWebSocketService;
	private final ChatRestService chatRestService;
	private final ChatStoreFacade chatStoreFacade;
	private final ChatPushFacade chatPushFacade;
	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/chat/send")
	public void sendMessage(ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) {
		// 현재 유저 정보
		StoreSession session = (StoreSession) headerAccessor.getSessionAttributes().get("storeSession");
		if (session == null) {
			log.info("storeSession is null! rejecting");
			return;
		}
		log.info("📨 Received chat.send: {}", request);
		log.info("📌 session info: {}", session);

		Long senderId = session.getId();

		List<String> deviceTokens = Collections.emptyList();
		String opponentNickname = null;
		Long opponentId = null;

		// TEXT, IMAGE만 푸시 쿼리 실행
		if (request.type() == MessageType.TEXT || request.type() == MessageType.IMAGE) {
			opponentId = chatRestService.findOpponentStoreId(request.roomId(), senderId);
			deviceTokens = chatPushFacade.findAllowMessageDeviceTokensByStoreId(opponentId);

			if (!deviceTokens.isEmpty()) {
				opponentNickname = chatStoreFacade.findNicknameByStoreId(senderId);
			}
		}

		chatWebSocketService.sendStoreMessage(
			request.roomId(), senderId, request.type(), request.content(),
			request.metadata(), deviceTokens, opponentNickname, opponentId);
	}

	@MessageMapping("/ping")
	public void handlePing() {
		log.info("🏓Received PING");
		messagingTemplate.convertAndSend("/topic/pong", "pong");
	}
}
