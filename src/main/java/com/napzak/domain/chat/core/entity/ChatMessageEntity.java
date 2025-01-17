package com.napzak.domain.chat.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.napzak.domain.chat.core.entity.ChatMessageTableConstants.*;

@Table(name = TABLE_CHAT_MESSAGE)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_CHAT_ROOM_ID, nullable = false)
	private Long chatRoomId;

	@Column(name = COLUMN_CONTENT, nullable = false)
	private String content;

	@Column(name = COLUMN_CREATED_AT, nullable = false)
	private LocalDateTime createdAt;

	@Column(name = COLUMN_IS_READ, nullable = false)
	private Boolean isRead;

	@Column(name = COLUMN_SENDER_ID, nullable = false)
	private Long senderId;

	@Builder
	private ChatMessageEntity(Long chatRoomId, String content, LocalDateTime createdAt, Boolean isRead, Long senderId) {
		this.chatRoomId = chatRoomId;
		this.content = content;
		this.createdAt = createdAt;
		this.isRead = isRead;
		this.senderId = senderId;
	}
}
