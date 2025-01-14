package com.napzak.domain.chat.core;

import com.napzak.domain.product.core.ProductEntity;
import com.napzak.domain.store.core.StoreEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoom {
    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final ProductEntity product;
    private final StoreEntity owner;
    private final StoreEntity requester;

    public ChatRoom(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, ProductEntity product, StoreEntity owner, StoreEntity requester) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.product = product;
        this.owner = owner;
        this.requester = requester;
    }

    public static ChatRoom fromEntity(ChatRoomEntity chatRoomEntity) {
        return new ChatRoom(
                chatRoomEntity.getId(),
                chatRoomEntity.getCreatedAt(),
                chatRoomEntity.getUpdatedAt(),
                chatRoomEntity.getProduct(),
                chatRoomEntity.getOwner(),
                chatRoomEntity.getRequester()
        );
    }
}