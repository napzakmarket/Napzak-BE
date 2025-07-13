package com.napzak.chat.domain.chat.api.service;

import java.util.List;

import com.napzak.chat.domain.chat.api.dto.request.cursor.ChatMessageCursor;
import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.code.ErrorCode;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.chat.entity.enums.ChatMessageSortOption;

import jakarta.annotation.Nullable;

public class ChatMessagePagination {
	private final int needSize;
	private final List<ChatMessage> messages;

	public ChatMessagePagination(int needSize, List<ChatMessage> messages) {
		this.needSize = needSize;
		this.messages = messages;
	}

	private boolean hasMoreData() {
		return messages.size() > needSize;
	}

	@Nullable
	public String generateNextCursor(ChatMessageSortOption sortOption) {
		if (!hasMoreData()) {
			return null;
		}
		ChatMessage lastMessage = messages.get(needSize);
		switch (sortOption) {
			case OLDEST:
				return new ChatMessageCursor(lastMessage.getId()).toString();
			default:
				throw new NapzakException(ErrorCode.INVALID_SORT_OPTION);
		}
	}

	public List<ChatMessage> getMessages() {
		return messages.subList(0, Math.min(needSize, messages.size()));
	}
}