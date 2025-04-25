package com.napzak.domain.store.api.dto.request;

import jakarta.validation.constraints.Size;

public record NicknameRequest(
	@Size(max = 20)
	String nickname
) {
}
