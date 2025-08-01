package com.napzak.chat.domain.chat.amqp;

import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.napzak.chat.domain.chat.api.service.ChatWebSocketService;
import com.napzak.common.auth.context.StoreSession;
import com.napzak.domain.chat.crud.chatpresence.ChatPresenceRedisRetriever;
import com.napzak.domain.chat.crud.chatpresence.ChatPresenceRedisUpdater;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {
	private final ChatWebSocketService chatWebSocketService;
	private final ChatPresenceRedisRetriever chatPresenceRedisRetriever;
	private final ChatPresenceRedisUpdater chatPresenceRedisUpdater;


	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		Map<String, Object> sessionAttributes = SimpMessageHeaderAccessor.wrap(event.getMessage()).getSessionAttributes();
		if (sessionAttributes == null || !sessionAttributes.containsKey("storeSession")) return;

		StoreSession session = (StoreSession) sessionAttributes.get("storeSession");
		Long userId = session.getId();

		Set<Long> roomIds = chatPresenceRedisRetriever.getUserRooms(userId);
		for (Long roomId : roomIds) {
			chatWebSocketService.sendLeaveBroadcast(roomId, userId);
		}

		chatPresenceRedisUpdater.removeUserFromAllRooms(userId);
	}
}
