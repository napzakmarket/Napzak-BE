package com.napzak.domain.chat.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.napzak.domain.chat.core.entity.ChatRoomTableConstants.*;

@Entity
@Table(name = TABLE_CHAT_ROOM)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_CREATED_AT, nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = COLUMN_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = COLUMN_PRODUCT_ID, nullable = false)
    private Long productId;

    @Column(name = COLUMN_OWNER_ID, nullable = false)
    private Long ownerId;

    @Column(name = COLUMN_REQUESTER_ID, nullable = false)
    private Long requesterId;

    @Builder
    private ChatRoomEntity(Long productId, Long ownerId, Long requesterId) {
        this.productId = productId;
        this.ownerId = ownerId;
        this.requesterId = requesterId;
    }
}