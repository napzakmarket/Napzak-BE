package com.napzak.api.domain.product.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.napzak.api.domain.product.dto.response.ProductSharePageView;

@Component
public class ProductShareHtmlRenderer {

	private static final Pattern PLACEHOLDER_PATTERN =
		Pattern.compile("\\{\\{([A-Za-z0-9_]+)\\}\\}");

	private final String appStoreUrl;
	private final String playStoreUrl;
	private final String appScheme;
	private final String template;

	public ProductShareHtmlRenderer(
		@Value("${napzak.share.app-store-url}") String appStoreUrl,
		@Value("${napzak.share.play-store-url}") String playStoreUrl,
		@Value("${napzak.share.app-scheme}") String appScheme,
		@Value("classpath:templates/share/product-share-page.html") Resource templateResource
	) {
		this.appStoreUrl = validateNotBlank(appStoreUrl, "napzak.share.app-store-url");
		this.playStoreUrl = validateNotBlank(playStoreUrl, "napzak.share.play-store-url");
		this.appScheme = validateNotBlank(appScheme, "napzak.share.app-scheme");
		this.template = loadTemplate(templateResource);
	}

	public String renderAvailable(ProductSharePageView view) {
		return render(
			view,
			"납작마켓 앱에서 확인할 수 있어요.",
			"앱을 설치하고 더 많은 취향 저격 아이템을 만나보세요."
		);
	}

	public String renderUnavailable(ProductSharePageView view) {
		return render(
			view,
			"삭제되었거나 더 이상 존재하지 않는 상품이에요.",
			"납작마켓에서 다른 상품을 둘러보세요."
		);
	}

	private String render(
		ProductSharePageView view,
		String bodyTitle,
		String bodyDescription
	) {
		String bodyDescriptionHtml = StringUtils.hasText(bodyDescription)
			? """
				<p class="share-subtitle">%s</p>
				""".formatted(escapeHtml(bodyDescription))
			: "";

		return replacePlaceholders(template, Map.of(
			"title", escapeHtml(view.title()),
			"description", escapeHtml(view.description()),
			"imageUrl", escapeHtml(view.imageUrl()),
			"pageUrl", escapeHtml(view.pageUrl()),
			"bodyTitle", escapeHtml(bodyTitle),
			"bodyDescriptionHtml", bodyDescriptionHtml,
			"appStoreUrl", escapeHtml(appStoreUrl),
			"playStoreUrl", escapeHtml(playStoreUrl),
			"appDeepLinkUrl", escapeJavaScript(buildAppDeepLinkUrl(view.productId()))
		));
	}

	private String buildAppDeepLinkUrl(Long productId) {
		if (productId == null) {
			throw new IllegalStateException("productId must not be null");
		}

		return appScheme + "://product/" + productId;
	}

	private String replacePlaceholders(String template, Map<String, String> values) {
		Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
		StringBuilder result = new StringBuilder();

		while (matcher.find()) {
			String placeholderName = matcher.group(1);
			String value = values.get(placeholderName);

			if (value == null) {
				throw new IllegalStateException("Missing template placeholder value: " + placeholderName);
			}

			matcher.appendReplacement(result, Matcher.quoteReplacement(value));
		}

		matcher.appendTail(result);

		return result.toString();
	}

	private String escapeHtml(String value) {
		if (value == null) {
			return "";
		}

		return HtmlUtils.htmlEscape(value);
	}

	private String escapeJavaScript(String value) {
		if (value == null) {
			return "";
		}

		return value
			.replace("\\", "\\\\")
			.replace("'", "\\'")
			.replace("\"", "\\\"")
			.replace("\n", "\\n")
			.replace("\r", "\\r")
			.replace("</", "<\\/");
	}

	private String validateNotBlank(String value, String propertyName) {
		if (!StringUtils.hasText(value)) {
			throw new IllegalStateException(propertyName + " must not be blank");
		}

		return value.trim();
	}

	private String loadTemplate(Resource templateResource) {
		try {
			return templateResource.getContentAsString(StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to load product share page template", e);
		}
	}
}