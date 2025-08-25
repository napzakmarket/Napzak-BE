package com.napzak.domain.store.crud.withdraw;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.entity.WithdrawEntity;
import com.napzak.domain.store.repository.WithdrawRepository;
import com.napzak.common.util.discord.DiscordWebhookSender;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WithdrawSaver {

	private final WithdrawRepository withdrawRepository;
	private final DiscordWebhookSender discordWebhookSender;

	@Transactional
	public void save(Long storeId, String title, String description, LocalDateTime createdAt) {
		WithdrawEntity entity = WithdrawEntity.create(storeId, title, description, createdAt);
		withdrawRepository.save(entity);

		discordWebhookSender.sendWithdraw("""
			------------------------------------------------------------------------------------------------------------------
			📤 __**회원 탈퇴 발생**__
			
			🫥 **탈퇴 회원 ID**: %s
			
			📝 **제목**: %s
			
			📄 **상세 내용**: %s
			
			🕒 **탈퇴 시각**: %s
			""".formatted(
			entity.getWithdrawerId(),
			entity.getTitle(),
			entity.getDescription(),
			entity.getCreatedAt()
		));

	}
}
