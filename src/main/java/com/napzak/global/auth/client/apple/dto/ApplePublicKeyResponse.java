package com.napzak.global.auth.client.apple.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplePublicKeyResponse {
	private List<ApplePublicKeyDto> keys;
}

