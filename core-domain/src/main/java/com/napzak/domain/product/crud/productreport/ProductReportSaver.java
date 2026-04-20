package com.napzak.domain.product.crud.productreport;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.common.util.slack.SlackWebhookSender;
import com.napzak.domain.product.entity.ProductReportEntity;
import com.napzak.domain.product.repository.ProductReportRepository;
import com.napzak.domain.product.vo.Product;
import com.napzak.domain.product.vo.ProductPhoto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductReportSaver {

	private final ProductReportRepository productReportRepository;
	private final SlackWebhookSender slackWebhookSender;

	@Transactional
	public void save(Long reporterId, Product product, List<ProductPhoto> photoList, String title, String description,
		String contact) {
		String allImageUrls = photoList.stream()
			.sorted(Comparator.comparingInt(ProductPhoto::getSequence))
			.map(ProductPhoto::getPhotoUrl)
			.collect(Collectors.joining("\n"));

		ProductReportEntity entity = ProductReportEntity.create(
			reporterId,
			product.getId(),
			allImageUrls,
			product.getTitle(),
			product.getDescription(),
			title,
			description,
			contact,
			LocalDateTime.now()
		);
		productReportRepository.save(entity);

		slackWebhookSender.sendProductReport("""
			----------------------------------------
			📦 *상품 신고 발생*

			• *신고자 ID:* %d
			• *상품 ID:* %d
			• *연락처:* %s
			• *신고 시각:* %s
			• *환경:* `%s`

			*상품 정보*
			• *상품 제목:* %s
			• *상품 설명:* %s

			*신고 내용*
			• *신고 사유:* %s

			*신고 상세*
			%s

			*상품 이미지 URL*
			%s
			""".formatted(
			entity.getReporterId(),
			entity.getReportedProductId(),
			entity.getReportContact(),
			entity.getCreatedAt(),
			slackWebhookSender.getCurrentEnvironment(),
			entity.getReportedProductTitle(),
			entity.getReportedProductDescription(),
			entity.getReportTitle(),
			entity.getReportDescription(),
			entity.getReportedProductImages()
		));
	}
}
