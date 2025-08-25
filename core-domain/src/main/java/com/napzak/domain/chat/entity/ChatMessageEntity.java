package com.napzak.domain.chat.entity;

import java.time.LocalDateTime;

import com.napzak.domain.chat.entity.enums.MessageType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = ChatMessageTableConstants.TABLE_CHAT_MESSAGE)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageEntity {

	@Id
	@Column(name = ChatMessageTableConstants.COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = ChatMessageTableConstants.COLUMN_ROOM_ID, nullable = false)
	private Long roomId;

	@Column(name = ChatMessageTableConstants.COLUMN_SENDER_ID, nullable = true)
	private Long senderId;

	@Column(name = ChatMessageTableConstants.COLUMN_TYPE, nullable = false, columnDefinition = "varchar(20)")
	@Enumerated(EnumType.STRING)
	private MessageType type;

	@Column(name = ChatMessageTableConstants.COLUMN_CONTENT, nullable = true, columnDefinition = "text")
	private String content;

	@Column(name = ChatMessageTableConstants.COLUMN_METADATA, nullable = true, columnDefinition = "json")
	private String metadata;

	@Column(name = ChatMessageTableConstants.COLUMN_CREATED_AT, nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	private ChatMessageEntity(Long roomId, Long senderId, MessageType type, String content, String metadata) {
		this.roomId = roomId;
		this.senderId = senderId;
		this.type = type;
		this.content = content;
		this.metadata = metadata;
	}

	public static ChatMessageEntity create(Long roomId, Long senderId, MessageType type, String content, String metadata) {
		return ChatMessageEntity.builder()
			.roomId(roomId)
			.senderId(senderId)
			.type(type)
			.content(content)
			.metadata(metadata)
			.build();
	}
}
