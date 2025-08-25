package com.napzak.domain.chat.entity;

import java.time.LocalDateTime;

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

@Table(name = ChatRoomTableConstants.TABLE_CHAT_ROOM)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEntity {

	@Id
	@Column(name = ChatRoomTableConstants.COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = ChatRoomTableConstants.COLUMN_PRODUCT_ID, nullable = false)
	private Long productId;

	@Column(name = ChatRoomTableConstants.COLUMN_CREATED_AT, nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	private ChatRoomEntity(Long productId) {
		this.productId = productId;
	}

	public static ChatRoomEntity create(Long productId) {
		return ChatRoomEntity.builder()
			.productId(productId)
			.build();
	}

	public void updateProductId(Long newProductId) {
		this.productId = newProductId;
	}
}