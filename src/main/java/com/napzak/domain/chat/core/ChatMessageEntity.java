package com.napzak.domain.chat.core;

import com.napzak.domain.store.core.StoreEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.napzak.domain.chat.core.ChatMessageTableConstants.*;

@Table(name = TABLE_CHAT_MESSAGE)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = COLUMN_CHAT_ROOM_ID, nullable = false)
    private ChatRoomEntity chatRoomEntity;

    @Column(name = COLUMN_CONTENT, nullable = false)
    private String content;

    @Column(name = COLUMN_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = COLUMN_IS_READ, nullable = false)
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = COLUMN_SENDER_ID, nullable = false)
    private StoreEntity sender;

    @Builder
    private ChatMessageEntity(ChatRoomEntity chatRoomEntity, String content, LocalDateTime createdAt, Boolean isRead, StoreEntity sender) {
        this.chatRoomEntity = chatRoomEntity;
        this.content = content;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.sender = sender;
    }

    public static ChatMessageEntity create(
            final ChatRoomEntity chatRoomEntity,
            final String content,
            final LocalDateTime createdAt,
            final Boolean isRead, StoreEntity sender
    ) {
        return ChatMessageEntity.builder()
                .chatRoomEntity(chatRoomEntity)
                .content(content)
                .createdAt(createdAt)
                .isRead(isRead)
                .sender(sender)
                .build();
    }

    public Boolean getIsRead() {
        return isRead;
    }


}