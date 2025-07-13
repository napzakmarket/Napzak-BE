package com.napzak.common.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.napzak.common.exception.base.BaseSuccessCode;

@JsonPropertyOrder({"status", "message", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SuccessResponse<T>(
	int status,
	String message,
	T data,
	String externalLink
) {
	// 성공 응답 (데이터 없음)
	public static <T> SuccessResponse<T> of(BaseSuccessCode baseSuccessCode) {
		return new SuccessResponse<>(baseSuccessCode.getHttpStatus().value(), baseSuccessCode.getMessage(), null, null);
	}

	// 성공 응답 (데이터 있음)
	public static <T> SuccessResponse<T> of(BaseSuccessCode baseSuccessCode, T data) {
		return new SuccessResponse<>(baseSuccessCode.getHttpStatus().value(), baseSuccessCode.getMessage(), data, null);
	}

	// 성공 응답 (외부 링크 포함)
	public static <T> SuccessResponse<T> of(BaseSuccessCode baseSuccessCode, T data, String externalLink) {
		return new SuccessResponse<>(baseSuccessCode.getHttpStatus().value(), baseSuccessCode.getMessage(), data, externalLink);
	}
}
