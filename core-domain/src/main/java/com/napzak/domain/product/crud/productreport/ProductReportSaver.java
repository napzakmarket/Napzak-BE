package com.napzak.domain.product.crud.productreport;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.product.entity.ProductReportEntity;
import com.napzak.domain.product.repository.ProductReportRepository;
import com.napzak.domain.product.vo.Product;
import com.napzak.domain.product.vo.ProductPhoto;
import com.napzak.common.util.discord.DiscordWebhookSender;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductReportSaver {

	private final ProductReportRepository productReportRepository;
	private final DiscordWebhookSender discordWebhookSender;

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

		discordWebhookSender.sendProductReport("""
			------------------------------------------------------------------------------------------------------------------
			📦 __**상품 신고 발생**__

			🧑‍💻 **신고자 ID:** %d

			📌 **신고 대상 상품 ID:** %d

			📸 **상품 이미지:** %s

			🏷 **상품 제목:** %s

			📝 **상품 설명:** %s

			🚨 **신고 사유:** %s

			📄 **신고 설명:** %s

			☎️ **연락처:** %s

			🕰 **신고 시각:** %s
			""".formatted(
			entity.getReporterId(),
			entity.getReportedProductId(),
			entity.getReportedProductImages(),
			entity.getReportedProductTitle(),
			entity.getReportedProductDescription(),
			entity.getReportTitle(),
			entity.getReportDescription(),
			entity.getReportContact(),
			entity.getCreatedAt()
		));
	}
}
