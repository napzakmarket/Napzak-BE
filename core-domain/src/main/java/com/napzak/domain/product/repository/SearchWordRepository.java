package com.napzak.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.napzak.domain.product.entity.SearchWordEntity;

@Repository
public interface SearchWordRepository extends JpaRepository<SearchWordEntity, Long> {
}
