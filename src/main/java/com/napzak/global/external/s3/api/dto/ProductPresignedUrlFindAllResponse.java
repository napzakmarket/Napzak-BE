package com.napzak.global.external.s3.api.dto;

import java.util.Map;

public record ProductPresignedUrlFindAllResponse(
	Map<String, String> productPresignedUrls
) {
	public static ProductPresignedUrlFindAllResponse from(
		Map<String, String> productPresignedUrls) {
		return new ProductPresignedUrlFindAllResponse(productPresignedUrls);
	}
}
