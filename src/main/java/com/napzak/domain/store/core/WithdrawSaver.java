package com.napzak.domain.store.core;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.core.entity.WithdrawEntity;
import com.napzak.global.common.util.discord.DiscordWebhookSender;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WithdrawSaver {

	private final WithdrawRepository withdrawRepository;
	private final DiscordWebhookSender discordWebhookSender;

	@Transactional
	public void save(String title, String description, LocalDateTime createdAt) {
		WithdrawEntity entity = WithdrawEntity.create(title, description, createdAt);
		withdrawRepository.save(entity);

		discordWebhookSender.sendWithdraw("""
			------------------------------------------------------------------------------------------------------------------
			📤 __**회원 탈퇴 발생**__
			
			📝 **제목**: %s
			
			📄 **상세 내용**: %s
			
			🕒 **탈퇴 시각**: %s
			""".formatted(
			entity.getTitle(),
			entity.getDescription(),
			entity.getCreatedAt()
		));

	}
}
