package com.napzak.domain.product.vo;

import com.napzak.domain.product.entity.ProductReportEntity;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProductReport {
	private final Long id;
	private final Long reporterId;
	private final Long reportedProductId;
	private final String reportedProductImages;
	private final String reportedProductTitle;
	private final String reportedProductDescription;
	private final String reportTitle;
	private final String reportDescription;
	private final String reportContact;
	private final LocalDateTime createdAt;

	public ProductReport(
		Long id,
		Long reporterId,
		Long reportedProductId,
		String reportedProductImages,
		String reportedProductTitle,
		String reportedProductDescription,
		String reportTitle,
		String reportDescription,
		String reportContact,
		LocalDateTime createdAt
	) {
		this.id = id;
		this.reporterId = reporterId;
		this.reportedProductId = reportedProductId;
		this.reportedProductImages = reportedProductImages;
		this.reportedProductTitle = reportedProductTitle;
		this.reportedProductDescription = reportedProductDescription;
		this.reportTitle = reportTitle;
		this.reportDescription = reportDescription;
		this.reportContact = reportContact;
		this.createdAt = createdAt;
	}

	public static ProductReport fromEntity(ProductReportEntity productReportEntity) {
		return new ProductReport(
			productReportEntity.getId(),
			productReportEntity.getReporterId(),
			productReportEntity.getReportedProductId(),
			productReportEntity.getReportedProductImages(),
			productReportEntity.getReportedProductTitle(),
			productReportEntity.getReportedProductDescription(),
			productReportEntity.getReportTitle(),
			productReportEntity.getReportDescription(),
			productReportEntity.getReportContact(),
			productReportEntity.getCreatedAt()
		);
	}
}
