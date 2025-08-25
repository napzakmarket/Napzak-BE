package com.napzak.domain.chat.repository;

import static com.napzak.domain.chat.entity.QChatMessageEntity.*;
import static com.napzak.domain.chat.entity.enums.MessageType.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.napzak.domain.chat.entity.ChatMessageEntity;
import com.napzak.domain.chat.entity.enums.MessageType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatMessageReporitoryCustomImpl implements ChatMessageRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public Map<Long, Long> findUnreadCountsByRoomIdsAndStoreId(
		List<Long> roomIds,
		Long storeId,
		Map<Long, Long> lastReadMap
	) {
		BooleanBuilder lastReadCondition = new BooleanBuilder();
		lastReadMap.forEach((roomId, lastReadId) -> {
			if (lastReadId != null) {
				lastReadCondition.or(
					chatMessageEntity.roomId.eq(roomId)
						.and(chatMessageEntity.id.gt(lastReadId))
				);
			} else {
				lastReadCondition.or(
					chatMessageEntity.roomId.eq(roomId)
				);
			}
		});
		try {
			List<Tuple> tuples = queryFactory
				.select(chatMessageEntity.roomId, chatMessageEntity.id.count())
				.from(chatMessageEntity)
				.where(
					chatMessageEntity.roomId.in(roomIds),
					chatMessageEntity.senderId.ne(storeId),
					chatMessageEntity.type.in(TEXT, IMAGE),
					lastReadCondition
				)
				.groupBy(chatMessageEntity.roomId)
				.fetch();
			Map<Long, Long> result = tuples.stream()
				.collect(Collectors.toMap(
					t -> t.get(chatMessageEntity.roomId),
					t -> Optional.ofNullable(t.get(chatMessageEntity.id.count())).orElse(0L)
				));
			return result;
		} catch (Exception e) {
			log.error("🔥 예외 발생: class={}, message={}", e.getClass().getName(), e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public Map<Long, ChatMessageEntity> findLastMessagesByRoomIds(List<Long> roomIds) {
		return queryFactory
			.selectFrom(chatMessageEntity)
			.where(chatMessageEntity.roomId.in(roomIds))
			.orderBy(chatMessageEntity.roomId.asc(), chatMessageEntity.createdAt.desc())
			.fetch()
			.stream()
			.collect(Collectors.toMap(
				ChatMessageEntity::getRoomId,
				e -> e,
				(first, second) -> first // 가장 최근 것만 유지
			));
	}

	@Override
	public List<ChatMessageEntity> findByRoomIdAndCursor(Long roomId, Long cursorId, int size) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(chatMessageEntity.roomId.eq(roomId));
		if (cursorId != null) {
			builder.and(chatMessageEntity.id.lt(cursorId));
		}
		return queryFactory
			.selectFrom(chatMessageEntity)
			.where(builder)
			.orderBy(chatMessageEntity.id.desc())
			.limit(size + 1) // hasMoreData 확인용 +1
			.fetch();
	}

	@Override
	public boolean existsDateMessageToday(Long roomId, LocalDate today) {
		return queryFactory
			.selectOne()
			.from(chatMessageEntity)
			.where(
				chatMessageEntity.roomId.eq(roomId),
				chatMessageEntity.type.eq(MessageType.DATE),
				chatMessageEntity.createdAt.goe(today.atStartOfDay())
			)
			.fetchFirst() != null;
	}
}
