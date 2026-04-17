package com.napzak.common.util.encryption;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "encryption.phone")
public class PhoneEncryptionProperties {
	private String aesKey;
	private String hmacSecret;
}
