package com.napzak.api.domain.file.dto;

import java.util.Map;

public record ChatPresignedUrlFindAllResponse (
	Map<String, String> chatPresignedUrls
){
	public static ChatPresignedUrlFindAllResponse from(
		Map<String, String> chatPresignedUrls) {
		return new ChatPresignedUrlFindAllResponse(chatPresignedUrls);
	}
}