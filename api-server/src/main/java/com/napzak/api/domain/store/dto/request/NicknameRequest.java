package com.napzak.api.domain.store.dto.request;

import jakarta.validation.constraints.Size;

public record NicknameRequest(
	@Size(max = 20)
	String nickname
) {
}
