package com.napzak.domain.chat.core;

import com.napzak.domain.product.core.ProductEntity;
import com.napzak.domain.store.core.StoreEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.napzak.domain.chat.core.ChatRoomTableConstants.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_PRODUCT_ID, nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_OWNER_ID, nullable = false)
    private StoreEntity owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_REQUESTER_ID, nullable = false)
    private StoreEntity requester;

    @Builder
    private ChatRoomEntity(ProductEntity product, StoreEntity owner, StoreEntity requester) {
        this.product = product;
        this.owner = owner;
        this.requester = requester;
    }

    public static ChatRoomEntity create(
            final ProductEntity product,
            final StoreEntity owner,
            final StoreEntity requester) {
        return ChatRoomEntity.builder()
                .product(product)
                .owner(owner)
                .requester(requester)
                .build();
    }
}