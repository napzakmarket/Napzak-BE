package com.napzak.domain.chat.core;

import com.napzak.domain.store.core.StoreEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessage {
    private final Long id;
    private final ChatRoom chatRoom;
    private final String content;
    private final LocalDateTime createdAt;
    private final boolean isRead;
    private final StoreEntity sender;

    public ChatMessage(Long id, ChatRoom chatRoom, String content, LocalDateTime createdAt, boolean isRead, StoreEntity sender) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.content = content;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.sender = sender;
    }

    public static ChatMessage fromEntity(ChatMessageEntity chatMessageEntity) {
        return new ChatMessage(
                chatMessageEntity.getId(),
                ChatRoom.fromEntity(chatMessageEntity.getChatRoom()),
                chatMessageEntity.getContent(),
                chatMessageEntity.getCreatedAt(),
                chatMessageEntity.getIsRead(),
                chatMessageEntity.getSender()
        );
    }


}