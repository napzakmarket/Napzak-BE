package com.napzak.domain.report.core.entity;

import static com.napzak.domain.report.core.entity.ProductReportTableConstants.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = TABLE_PRODUCT_REPORT)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductReportEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_REPORTER_ID, nullable = false)
	private Long reporterId;

	@Column(name = COLUMN_REPORTED_PRODUCT_ID, nullable = false)
	private Long reportedProductId;

	@Column(name = COLUMN_REPORTED_PRODUCT_IMAGES, nullable = false)
	private String reportedProductImages;

	@Column(name = COLUMN_REPORTED_PRODUCT_TITLE, nullable = false)
	private String reportedProductTitle;

	@Column(name = COLUMN_REPORTED_PRODUCT_DESCRIPTION, nullable = false)
	private String reportedProductDescription;

	@Column(name = COLUMN_REPORT_TITLE, nullable = false)
	private String reportTitle;

	@Column(name = COLUMN_REPORT_DESCRIPTION, nullable = false)
	private String reportDescription;

	@Column(name = COLUMN_REPORT_CONTACT, nullable = false)
	private String reportContact;

	@Column(name = COLUMN_CREATED_AT, nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	public ProductReportEntity(
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

	public static ProductReportEntity create(
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
		return ProductReportEntity.builder()
			.reporterId(reporterId)
			.reportedProductId(reportedProductId)
			.reportedProductImages(reportedProductImages)
			.reportedProductTitle(reportedProductTitle)
			.reportedProductDescription(reportedProductDescription)
			.reportTitle(reportTitle)
			.reportDescription(reportDescription)
			.reportContact(reportContact)
			.createdAt(createdAt)
			.build();
	}
}
