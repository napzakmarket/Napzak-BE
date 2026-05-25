package com.napzak.common.util.slack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackWebhookSender {

	private final SlackWebhookProperties webhookProperties;
	private final RestTemplate restTemplate;
	private final Environment environment;

	public void send(String webhookUrl, String text) {
		if (webhookUrl == null || webhookUrl.isBlank()) {
			log.error("Slack webhook url is empty. Skip sending message.");
			return;
		}

		if (TransactionSynchronizationManager.isSynchronizationActive()
			&& TransactionSynchronizationManager.isActualTransactionActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					sendImmediately(webhookUrl, text);
				}
			});
			return;
		}

		sendImmediately(webhookUrl, text);
	}

	private void sendImmediately(String webhookUrl, String text) {
		Map<String, Object> body = Map.of("text", text);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		try {
			ResponseEntity<String> response = restTemplate.postForEntity(
				webhookUrl,
				new HttpEntity<>(body, headers),
				String.class
			);

			if (!response.getStatusCode().is2xxSuccessful()) {
				log.error("Failed to send slack webhook. status={}, body={}",
					response.getStatusCode(), response.getBody());
			}
		} catch (RestClientException exception) {
			log.error("Failed to send slack webhook. message={}", exception.getMessage(), exception);
		}
	}

	public String getCurrentEnvironment() {
		List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());

		if (activeProfiles.contains("prod")) {
			return "prod";
		}

		if (activeProfiles.contains("dev")) {
			return "dev";
		}

		if (activeProfiles.isEmpty()) {
			return "local";
		}

		return String.join(",", activeProfiles);
	}

	public void sendWithdraw(String message) {
		send(webhookProperties.withdraw(), message);
	}

	public void sendStoreReport(String message) {
		send(webhookProperties.storeReport(), message);
	}

	public void sendProductReport(String message) {
		send(webhookProperties.productReport(), message);
	}

	public void sendSignup(String message) {
		send(webhookProperties.signup(), message);
	}
}
