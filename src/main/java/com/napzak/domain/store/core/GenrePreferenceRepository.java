package com.napzak.domain.store.core;

import com.napzak.domain.store.core.entity.GenrePreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenrePreferenceRepository extends JpaRepository<GenrePreferenceEntity, Long> {

    boolean existsByStoreEntityId(Long storeId);

    List<GenrePreferenceEntity> findByStoreEntityId(Long storeId);
}


