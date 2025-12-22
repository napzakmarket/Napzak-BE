package com.napzak.domain.store.crud.storereport;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.common.util.slack.SlackWebhookSender;
import com.napzak.domain.store.entity.StoreReportEntity;
import com.napzak.domain.store.repository.StoreReportRepository;
import com.napzak.domain.store.vo.Store;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreReportSaver {

	private final StoreReportRepository storeReportRepository;
	private final SlackWebhookSender slackWebhookSender;

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

		slackWebhookSender.sendStoreReport("""
			----------------------------------------
			📣 *상점 신고 발생*

			• *신고자 ID:* %d
			• *상점 ID:* %d
			• *닉네임:* %s
			• *연락처:* %s
			• *신고 시각:* %s

			*상점 정보*
			• *프로필:* %s
			• *커버:* %s
			• *상점 소개:* %s

			*신고 내용*
			• *신고 사유:* %s

			*신고 상세*
			%s
			""".formatted(
			entity.getReporterId(),
			entity.getReportedStoreId(),
			entity.getReportedStoreNickname(),
			entity.getReportContact(),
			entity.getCreatedAt(),
			entity.getReportedStoreProfile(),
			entity.getReportedStoreCover(),
			entity.getReportedStoreDescription(),
			entity.getReportTitle(),
			entity.getReportDescription()
		));
	}
}
