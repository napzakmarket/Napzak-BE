package com.napzak.domain.product.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.napzak.domain.product.core.entity.SearchWordEntity;

@Repository
public interface SearchWordRepository extends JpaRepository<SearchWordEntity, Long> {
}
