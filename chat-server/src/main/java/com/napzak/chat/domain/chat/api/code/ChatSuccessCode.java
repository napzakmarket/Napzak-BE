package com.napzak.chat.domain.chat.api.code;

import org.springframework.http.HttpStatus;

import com.napzak.common.exception.base.BaseSuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatSuccessCode implements BaseSuccessCode {
	/*
	200 Ok
	 */
	MESSAGE_RETRIEVE_SUCCESS(HttpStatus.OK, "메세지가 조회되었습니다."),
	MESSAGE_LIST_RETRIEVE_SUCCESS(HttpStatus.OK, "메세지 리스트가 조회되었습니다."),
	CHATROOM_ENTER_SUCCESS(HttpStatus.OK, "채팅방 입장에 성공했습니다."),
	CHATROOM_LEAVE_SUCCESS(HttpStatus.OK, "채팅방 퇴장에 성공했습니다."),
	CHATROOM_EXIT_SUCCESS(HttpStatus.OK, "채팅방 영구 퇴장에 성공했습니다."),
	CHATROOM_LIST_RETRIEVE_SUCCESS(HttpStatus.OK, "채팅방 리스트가 조회되었습니다."),
	CHATROOM_PRODUCT_UPDATE_SUCCESS(HttpStatus.OK, "채팅방 상품 업데이트에 성공했습니다."),

	/*
	201 Created
	 */
	MESSAGE_CREATE_SUCCESS(HttpStatus.CREATED, "메세지가 등록되었습니다."),
	CHATROOM_CREATE_SUCCESS(HttpStatus.CREATED, "채팅방이 등록되었습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
