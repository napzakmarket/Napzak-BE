package com.napzak.domain.product.core;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.napzak.domain.product.core.HashtagTableConstants.*;

@Table(name = TABLE_HASHTAG)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashtagEntity {

    @Id
    @Column(name = COLUMN_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = COLUMN_NAME, nullable = false, unique = true)
    private String name;

    @Builder
    private HashtagEntity(String name) {
        this.name = name;
    }

    public static HashtagEntity create(final String name) {
        return HashtagEntity.builder()
                .name(name)
                .build();
    }
}
