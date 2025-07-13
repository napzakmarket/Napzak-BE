package com.napzak.common.auth.client.apple.dto;

import lombok.Getter;

@Getter
public class ApplePublicKeyDto{
	private String kty;
	private String kid;
	private String use;
	private String alg;
	private String n;
	private String e;
}
