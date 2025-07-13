package com.napzak.common.util.discord;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discord.webhook")
public record DiscordWebhookProperties(
	String withdraw,
	String storeReport,
	String productReport
) {}
