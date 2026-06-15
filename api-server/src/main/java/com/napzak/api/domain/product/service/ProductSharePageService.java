package com.napzak.api.domain.product.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.napzak.api.domain.product.dto.response.ProductDetailDto;
import com.napzak.api.domain.product.dto.response.ProductPhotoDto;
import com.napzak.api.domain.product.dto.response.ProductSharePageView;
import com.napzak.common.exception.NapzakException;
import com.napzak.domain.product.vo.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSharePageService {

	private static final String SITE_NAME = "납작마켓";
	private static final String DEFAULT_DESCRIPTION = "납작마켓 앱에서 상품을 확인해 주세요.";
	private static final String UNAVAILABLE_TITLE = "이미 판매되었거나 삭제된 상품이에요 | 납작마켓";
	private static final String UNAVAILABLE_DESCRIPTION = "납작마켓 앱에서 다양한 상품을 둘러보세요.";
	private static final int OG_DESCRIPTION_MAX_LENGTH = 100;

	private final ProductService productService;
	private final ProductShareUrlProvider productShareUrlProvider;
	private final ProductShareHtmlRenderer productShareHtmlRenderer;

	@Value("${napzak.share.default-og-image-url}")
	private String defaultOgImageUrl;

	@Value("${napzak.share.unavailable-og-image-url}")
	private String unavailableOgImageUrl;

	@Transactional(readOnly = true)
	public String renderProductSharePage(Long productId) {
		Product product = findVisibleProductOrNull(productId);

		if (product == null) {
			return renderUnavailableProductPage(productId);
		}

		return renderAvailableProductPage(productId, product);
	}

	private Product findVisibleProductOrNull(Long productId) {
		try {
			return productService.getProduct(productId);
		} catch (NapzakException e) {
			return null;
		}
	}

	private String renderAvailableProductPage(Long productId, Product product) {
		ProductDetailDto productDetailDto = ProductDetailDto.from(
			product,
			"",
			"",
			false
		);

		String pageUrl = productShareUrlProvider.generate(productId);
		String title = buildTitle(productDetailDto.productName());
		String description = buildDescription(productDetailDto.description());
		String imageUrl = findOgImageUrl(productId);

		ProductSharePageView view = new ProductSharePageView(
			title,
			description,
			imageUrl,
			pageUrl
		);

		return productShareHtmlRenderer.renderAvailable(view);
	}

	private String renderUnavailableProductPage(Long productId) {
		String pageUrl = productShareUrlProvider.generate(productId);

		ProductSharePageView view = new ProductSharePageView(
			UNAVAILABLE_TITLE,
			UNAVAILABLE_DESCRIPTION,
			unavailableOgImageUrl,
			pageUrl
		);

		return productShareHtmlRenderer.renderUnavailable(view);
	}

	private String findOgImageUrl(Long productId) {
		List<ProductPhotoDto> productPhotoList = productService.getProductPhotos(productId).stream()
			.map(ProductPhotoDto::from)
			.sorted(Comparator.comparingInt(ProductPhotoDto::sequence))
			.toList();

		if (productPhotoList.isEmpty()) {
			return defaultOgImageUrl;
		}

		String firstPhotoUrl = productPhotoList.get(0).photoUrl();

		if (!StringUtils.hasText(firstPhotoUrl)) {
			return defaultOgImageUrl;
		}

		return firstPhotoUrl;
	}

	private String buildTitle(String productName) {
		if (!StringUtils.hasText(productName)) {
			return SITE_NAME;
		}

		return productName.trim() + " | " + SITE_NAME;
	}

	private String buildDescription(String description) {
		if (!StringUtils.hasText(description)) {
			return DEFAULT_DESCRIPTION;
		}

		String trimmedDescription = description.trim();

		if (trimmedDescription.length() <= OG_DESCRIPTION_MAX_LENGTH) {
			return trimmedDescription;
		}

		return trimmedDescription.substring(0, OG_DESCRIPTION_MAX_LENGTH) + "…";
	}
}