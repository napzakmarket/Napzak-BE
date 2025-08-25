package com.napzak.chat.domain.chat.api.dto.request.cursor;

import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.code.ErrorCode;

public class ChatMessageCursor {
	private final Long id;

	public ChatMessageCursor(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "M-" + id;
	}

	public static ChatMessageCursor fromString(String cursor) {
		if (!cursor.startsWith("M-")) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
		String[] split = cursor.split("-");
		if (split.length != 2) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
		try {
			long id = Long.parseLong(split[1]);
			return new ChatMessageCursor(id);
		} catch (NumberFormatException e) {
			throw new NapzakException(ErrorCode.INVALID_CURSOR_FORMAT);
		}
	}
}