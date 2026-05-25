package com.napzak.common.util.slack;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "slack.webhook")
public record SlackWebhookProperties(
	String withdraw,
	String storeReport,
	String productReport,
	String signup
) {}
