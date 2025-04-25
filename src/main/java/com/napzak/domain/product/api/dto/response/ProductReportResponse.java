package com.napzak.domain.product.api.dto.response;

public record ProductReportResponse(
	Long reporterId,
	Long reportedProductId,
	String reportTitle,
	String reportDescription,
	String reportContact
) {
	public static ProductReportResponse of(
		Long reporterId,
		Long reportedProductId,
		String reportTitle,
		String reportDescription,
		String reportContact
	) {
		return new ProductReportResponse(reporterId, reportedProductId, reportTitle, reportDescription, reportContact);
	}
}
