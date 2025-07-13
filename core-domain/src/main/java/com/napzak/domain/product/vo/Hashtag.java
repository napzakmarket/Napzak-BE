package com.napzak.domain.product.vo;

import com.napzak.domain.product.entity.HashtagEntity;

import lombok.Getter;

@Getter
public class Hashtag {
    private final Long id;
    private final String name;

    public Hashtag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Hashtag fromEntity(HashtagEntity hashtagEntity) {
        return new Hashtag(
                hashtagEntity.getId(),
                hashtagEntity.getName()
        );
    }
}