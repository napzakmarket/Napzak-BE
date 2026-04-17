package com.napzak.common.util.encryption;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.napzak.common.exception.NapzakException;
import com.napzak.common.exception.code.ErrorCode;

@Component
public class PhoneEncryptionUtil {

	private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
	private static final String HMAC_ALGORITHM = "HmacSHA256";
	private static final int IV_LENGTH = 12;
	private static final int GCM_TAG_LENGTH = 128;

	private final SecretKey aesKey;
	private final SecretKey hmacKey;

	public PhoneEncryptionUtil(PhoneEncryptionProperties properties) {
		byte[] aesKeyBytes = Base64.getDecoder().decode(properties.getAesKey());
		this.aesKey = new SecretKeySpec(aesKeyBytes, "AES");
		byte[] hmacKeyBytes = Base64.getDecoder().decode(properties.getHmacSecret());
		this.hmacKey = new SecretKeySpec(hmacKeyBytes, HMAC_ALGORITHM);
	}

	public String encrypt(String plaintext) {
		try {
			byte[] iv = new byte[IV_LENGTH];
			new SecureRandom().nextBytes(iv);

			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
			byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

			byte[] combined = new byte[IV_LENGTH + encrypted.length];
			System.arraycopy(iv, 0, combined, 0, IV_LENGTH);
			System.arraycopy(encrypted, 0, combined, IV_LENGTH, encrypted.length);

			return Base64.getEncoder().encodeToString(combined);
		} catch (Exception e) {
			throw new NapzakException(ErrorCode.ENCRYPTION_FAILED);
		}
	}

	public String decrypt(String ciphertext) {
		try {
			byte[] combined = Base64.getDecoder().decode(ciphertext);
			byte[] iv = Arrays.copyOfRange(combined, 0, IV_LENGTH);
			byte[] encrypted = Arrays.copyOfRange(combined, IV_LENGTH, combined.length);

			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

			return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new NapzakException(ErrorCode.DECRYPTION_FAILED);
		}
	}

	public String hash(String plaintext) {
		try {
			Mac mac = Mac.getInstance(HMAC_ALGORITHM);
			mac.init(hmacKey);
			byte[] hashBytes = mac.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(hashBytes);
		} catch (Exception e) {
			throw new NapzakException(ErrorCode.HASHING_FAILED);
		}
	}
}
