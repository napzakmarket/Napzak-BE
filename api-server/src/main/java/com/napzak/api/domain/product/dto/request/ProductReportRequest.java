package com.napzak.api.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductReportRequest(
	@NotBlank String reportTitle,
	@NotBlank
	@Size(max = 200)
	String reportDescription,
	@NotBlank String reportContact
) {
}
