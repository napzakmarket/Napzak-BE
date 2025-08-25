package com.napzak.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = HashtagTableConstants.TABLE_HASHTAG)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashtagEntity {

    @Id
    @Column(name = HashtagTableConstants.COLUMN_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = HashtagTableConstants.COLUMN_NAME, nullable = false, unique = true)
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
