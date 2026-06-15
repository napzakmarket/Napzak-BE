package com.napzak.api.domain.product.dto.response;

public record ProductSharePageView(
	String title,
	String description,
	String imageUrl,
	String pageUrl
) {
}