package com.napzak.domain.store.crud.storereport;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.domain.store.entity.StoreReportEntity;
import com.napzak.domain.store.repository.StoreReportRepository;
import com.napzak.domain.store.vo.Store;
import com.napzak.common.util.discord.DiscordWebhookSender;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreReportSaver {

	private final StoreReportRepository storeReportRepository;
	private final DiscordWebhookSender discordWebhookSender;

	@Transactional
	public void save(Long reporterId, Store store, String title, String description, String contact) {
		StoreReportEntity entity = StoreReportEntity.create(
			reporterId,
			store.getId(),
			store.getNickname(),
			store.getPhoto(),
			store.getCover(),
			store.getDescription(),
			title,
			description,
			contact,
			LocalDateTime.now()
		);
		storeReportRepository.save(entity);

		discordWebhookSender.sendStoreReport("""
			------------------------------------------------------------------------------------------------------------------
			📣 __**상점 신고 발생**__

			🆔 **신고자 ID:** %d

			🏪 **상점 ID:** %d

			📛 **닉네임:** %s

			🖼️ **프로필:** %s

			🖼️ **커버:** %s

			📝 **상점 소개:** %s

			🚨 **신고 사유:** %s

			📄 **신고 상세:** %s

			📱 **연락처:** %s

			🕒 **신고 시각:** %s
			""".formatted(
			entity.getReporterId(),
			entity.getReportedStoreId(),
			entity.getReportedStoreNickname(),
			entity.getReportedStoreProfile(),
			entity.getReportedStoreCover(),
			entity.getReportedStoreDescription(),
			entity.getReportTitle(),
			entity.getReportDescription(),
			entity.getReportContact(),
			entity.getCreatedAt()
		));
	}
}
