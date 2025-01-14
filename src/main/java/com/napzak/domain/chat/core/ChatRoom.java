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
    private final ProductEntity productEntity;
    private final StoreEntity owner;
    private final StoreEntity requester;

    public ChatRoom(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, ProductEntity productEntity, StoreEntity owner, StoreEntity requester) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.productEntity = productEntity;
        this.owner = owner;
        this.requester = requester;
    }

    public static ChatRoom fromEntity(ChatRoomEntity chatRoomEntity) {
        return new ChatRoom(
                chatRoomEntity.getId(),
                chatRoomEntity.getCreatedAt(),
                chatRoomEntity.getUpdatedAt(),
                chatRoomEntity.getProductEntity(),
                chatRoomEntity.getOwner(),
                chatRoomEntity.getRequester()
        );
    }
}