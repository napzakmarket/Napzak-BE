package com.napzak.chat.domain.chat.api.dto.request;

import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.napzak.domain.chat.entity.enums.MessageType;

import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatMessageRequest (
	@NotNull
	Long roomId,
	@NotNull
	MessageType type,
	@Nullable
	String content,
	@Nullable
	Map<String, Object> metadata
){
}