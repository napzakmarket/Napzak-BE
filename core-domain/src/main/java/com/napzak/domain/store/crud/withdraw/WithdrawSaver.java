package com.napzak.domain.store.crud.withdraw;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.common.util.slack.SlackWebhookSender;
import com.napzak.domain.store.entity.WithdrawEntity;
import com.napzak.domain.store.repository.WithdrawRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WithdrawSaver {

	private final WithdrawRepository withdrawRepository;
	private final SlackWebhookSender slackWebhookSender;

	@Transactional
	public void save(Long storeId, String title, String description, LocalDateTime createdAt, String phoneNumber) {
		WithdrawEntity entity = WithdrawEntity.create(storeId, title, description, createdAt, phoneNumber);
		withdrawRepository.save(entity);

		slackWebhookSender.sendWithdraw("""
			----------------------------------------
			📤 *회원 탈퇴 발생*

			• *탈퇴 회원 ID:* %s
			• *제목:* %s
			• *탈퇴 시각:* %s

			*상세 내용*
			%s
			""".formatted(
			entity.getWithdrawerId(),
			entity.getTitle(),
			entity.getCreatedAt(),
			entity.getDescription()
		));

	}
}
