package com.napzak.domain.store.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StoreReportRequest(
	@NotBlank
	String reportTitle,
	@NotBlank
	@Size(max = 200)
	String reportDescription,
	@NotBlank
	String reportContact
) {
}
