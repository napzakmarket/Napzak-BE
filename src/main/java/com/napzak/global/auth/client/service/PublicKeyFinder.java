package com.napzak.global.auth.client.service;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import com.napzak.global.auth.client.apple.dto.ApplePublicKeyDto;
import com.napzak.global.auth.client.exception.OAuthErrorCode;
import com.napzak.global.common.exception.NapzakException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PublicKeyFinder extends SigningKeyResolverAdapter {

	private final List<ApplePublicKeyDto> applePublicKeyList;

	@Override
	public Key resolveSigningKey(JwsHeader header, Claims claims) {
		return findKey(header);
	}

	@Override
	public Key resolveSigningKey(JwsHeader header, String plaintext) {
		return findKey(header);
	}

	private Key findKey(JwsHeader header) {
		Optional<ApplePublicKeyDto> optionalPublicKey = applePublicKeyList.stream()
			.filter(applePublicKey -> applePublicKey.getKid().equals(header.getKeyId()))
			.findFirst();

		if (optionalPublicKey.isEmpty()) {
			throw new NapzakException(OAuthErrorCode.INVALID_APPLE_ID_TOKEN);
		}

		ApplePublicKeyDto publicKey = optionalPublicKey.get();

		try {
			BigInteger n = new BigInteger(1, Base64.getUrlDecoder().decode(publicKey.getN()));
			BigInteger e = new BigInteger(1, Base64.getUrlDecoder().decode(publicKey.getE()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			KeySpec keySpec = new RSAPublicKeySpec(n, e);
			return keyFactory.generatePublic(keySpec);
		} catch (Exception error) {
			throw new NapzakException(OAuthErrorCode.APPLE_PUBLIC_KEY_EXTRACTION_FAILED);
		}
	}
}
