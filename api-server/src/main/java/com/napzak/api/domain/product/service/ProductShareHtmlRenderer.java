package com.napzak.api.domain.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import com.napzak.api.domain.product.dto.response.ProductSharePageView;

@Component
public class ProductShareHtmlRenderer {

	private final String appStoreUrl;
	private final String playStoreUrl;

	public ProductShareHtmlRenderer(
		@Value("${napzak.share.app-store-url}") String appStoreUrl,
		@Value("${napzak.share.play-store-url}") String playStoreUrl
	) {
		this.appStoreUrl = appStoreUrl;
		this.playStoreUrl = playStoreUrl;
	}

	public String renderAvailable(ProductSharePageView view) {
		return render(
			view,
			"납작마켓 앱에서 상품을 확인해 주세요.",
			"앱을 설치하면 상품 상세 정보를 바로 볼 수 있어요."
		);
	}

	public String renderUnavailable(ProductSharePageView view) {
		return render(
			view,
			"이미 판매되었거나 삭제된 상품이에요.",
			"납작마켓 앱에서 다른 상품을 둘러보세요."
		);
	}

	private String render(
		ProductSharePageView view,
		String bodyTitle,
		String bodyDescription
	) {
		String title = escape(view.title());
		String description = escape(view.description());
		String imageUrl = escape(view.imageUrl());
		String pageUrl = escape(view.pageUrl());
		String escapedBodyTitle = escape(bodyTitle);
		String escapedBodyDescription = escape(bodyDescription);

		String bodyDescriptionHtml = bodyDescription == null || bodyDescription.isBlank()
			? ""
			: "<p style=\"font-size:16px; line-height:1.5; margin:0 0 28px; color:#666;\">"
			  + escapedBodyDescription
			  + "</p>";

		return String.format("""
			<!DOCTYPE html>
			<html lang="ko">
			<head>
			    <meta charset="UTF-8" />
			    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

			    <meta property="og:title" content="%s" />
			    <meta property="og:description" content="%s" />
			    <meta property="og:image" content="%s" />
			    <meta property="og:url" content="%s" />
			    <meta property="og:type" content="website" />
			    <meta property="og:site_name" content="납작마켓" />

			    <meta name="twitter:card" content="summary_large_image" />
			    <meta name="twitter:title" content="%s" />
			    <meta name="twitter:description" content="%s" />
			    <meta name="twitter:image" content="%s" />

			    <title>%s</title>
			</head>
			<body style="margin:0;">
			    <main style="
			        min-height:100vh;
			        display:flex;
			        flex-direction:column;
			        align-items:center;
			        justify-content:center;
			        padding:40px 20px;
			        box-sizing:border-box;
			        text-align:center;
			        font-family:-apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
			    ">
			        <h1 style="font-size:24px; line-height:1.4; margin:0 0 12px;">
			            %s
			        </h1>

			        %s

			        <div style="display:flex; flex-direction:column; gap:12px; width:100%%; max-width:320px; margin-top:16px;">
			            <a href="%s" style="
			                display:block;
			                padding:14px 16px;
			                border-radius:10px;
			                background:#111;
			                color:#fff;
			                text-decoration:none;
			                font-size:16px;
			                font-weight:600;
			            ">App Store에서 다운로드</a>

			            <a href="%s" style="
			                display:block;
			                padding:14px 16px;
			                border-radius:10px;
			                background:#111;
			                color:#fff;
			                text-decoration:none;
			                font-size:16px;
			                font-weight:600;
			            ">Google Play에서 다운로드</a>
			        </div>
			    </main>
			</body>
			</html>
			""",
			title,
			description,
			imageUrl,
			pageUrl,
			title,
			description,
			imageUrl,
			title,
			escapedBodyTitle,
			bodyDescriptionHtml,
			escape(appStoreUrl),
			escape(playStoreUrl)
		);
	}

	private String escape(String value) {
		if (value == null) {
			return "";
		}
		return HtmlUtils.htmlEscape(value);
	}
}