package com.napzak.domain.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = ChatParticipantTableConstants.TABLE_CHAT_PARTICIPANT)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatParticipantEntity {

	@Id
	@Column(name = ChatParticipantTableConstants.COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = ChatParticipantTableConstants.COLUMN_ROOM_ID, nullable = false)
	private Long roomId;

	@Column(name = ChatParticipantTableConstants.COLUMN_STORE_ID, nullable = false)
	private Long storeId;

	@Column(name = ChatParticipantTableConstants.COLUMN_LAST_READ_MESSAGE_ID, nullable = true)
	private Long lastReadMessageId;

	// 채팅방 진입 여부
	@Column(name = ChatParticipantTableConstants.COLUMN_IS_LEAVED, nullable = false)
	private Boolean isLeaved = true;

	// 채팅방 구독 여부
	@Column(name = ChatParticipantTableConstants.COLUMN_IS_EXITED, nullable = false)
	private Boolean isExited = false;

	@Builder
	private ChatParticipantEntity(Long roomId, Long storeId, Long lastReadMessageId, Boolean isLeaved, Boolean isExited) {
		this.roomId = roomId;
		this.storeId = storeId;
		this.lastReadMessageId = lastReadMessageId;
		this.isLeaved = isLeaved;
		this.isExited = isExited;
	}

	public static ChatParticipantEntity create(Long roomId, Long storeId) {
		return ChatParticipantEntity.builder()
			.roomId(roomId)
			.storeId(storeId)
			.isLeaved(true)
			.isExited(false)
			.build();
	}

	public void exit() {
		this.isExited = true;
	}

	public void updateLastReadMessageId(Long messageId) {
		this.lastReadMessageId = messageId;
	}

	public void enter(Long messageId){
		this.lastReadMessageId = messageId;
		this.isLeaved = false;
	}

	public void leave(Long messageId){
		this.lastReadMessageId = messageId;
		this.isLeaved = true;
	}
}
