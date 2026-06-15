package com.napzak.api.domain.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductShareUrlProvider {

	private final String shareBaseUrl;

	public ProductShareUrlProvider(
		@Value("${napzak.share.base-url}") String shareBaseUrl
	) {
		this.shareBaseUrl = removeTrailingSlash(shareBaseUrl);
	}

	public String generate(Long productId) {
		return shareBaseUrl + "/product/" + productId;
	}

	private String removeTrailingSlash(String url) {
		if (url == null || url.isBlank()) {
			return "";
		}

		if (url.endsWith("/")) {
			return url.substring(0, url.length() - 1);
		}

		return url;
	}
}