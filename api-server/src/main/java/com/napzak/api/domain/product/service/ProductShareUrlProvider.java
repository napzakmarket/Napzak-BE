package com.napzak.api.domain.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductShareUrlProvider {

	private final String shareBaseUrl;

	public ProductShareUrlProvider(
		@Value("${napzak.share.base-url}") String shareBaseUrl
	) {
		String normalizedShareBaseUrl = removeTrailingSlash(shareBaseUrl);

		if (normalizedShareBaseUrl.isBlank()) {
			throw new IllegalStateException("napzak.share.base-url must not be blank");
		}

		this.shareBaseUrl = normalizedShareBaseUrl;
	}

	public String generate(Long productId) {
		return shareBaseUrl + "/product/" + productId;
	}

	private String removeTrailingSlash(String url) {
		if (url == null || url.isBlank()) {
			return "";
		}

		String trimmedUrl = url.trim();

		if (trimmedUrl.endsWith("/")) {
			return trimmedUrl.substring(0, trimmedUrl.length() - 1);
		}

		return trimmedUrl;
	}
}