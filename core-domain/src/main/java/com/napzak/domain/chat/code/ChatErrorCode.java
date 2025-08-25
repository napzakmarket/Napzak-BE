package com.napzak.domain.chat.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements BaseErrorCode {
	/*
	400 Bad Request
	*/
	INVALID_METADATA_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 metadata입니다."),

	/*
	403 Forbidden
	 */
	NO_CHATROOM_ACCESS(HttpStatus.FORBIDDEN, "해당 채팅방에 접근 권한이 없습니다."),

	/*
	404 Not Found
	 */
	CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
	MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메세지를 찾을 수 없습니다."),
	PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방 참여자를 찾을 수 없습니다."),

	/*
	409 Conflict
	 */
	CHATROOM_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 채팅방이 있습니다."),

	/*
	500 Internal Server Error
	 */
	MESSAGE_UNDELIVERED(HttpStatus.INTERNAL_SERVER_ERROR, "메시지 전송에 실패하여 재전송합니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
