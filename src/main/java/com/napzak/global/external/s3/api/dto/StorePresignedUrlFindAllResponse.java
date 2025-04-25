package com.napzak.global.external.s3.api.dto;

import java.util.Map;

public record StorePresignedUrlFindAllResponse(
	Map<String, String> profilePresignedUrls
) {
	public static StorePresignedUrlFindAllResponse from(
		Map<String, String> profilePresignedUrls) {
		return new StorePresignedUrlFindAllResponse(profilePresignedUrls);
	}
}
