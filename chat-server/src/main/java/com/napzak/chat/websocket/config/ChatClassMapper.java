package com.napzak.chat.websocket.config;

import org.springframework.amqp.support.converter.DefaultClassMapper;

import java.util.HashMap;
import java.util.Map;

public class ChatClassMapper extends DefaultClassMapper {
	public ChatClassMapper() {
		Map<String, Class<?>> idClassMapping = new HashMap<>();
		try {
			idClassMapping.put("com.napzak.domain.chat.vo.ChatMessagePayload",
				Class.forName("com.napzak.domain.chat.vo.ChatMessagePayload"));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("ChatMessagePayload 클래스 로딩 실패", e);
		}
		setIdClassMapping(idClassMapping);
	}
}