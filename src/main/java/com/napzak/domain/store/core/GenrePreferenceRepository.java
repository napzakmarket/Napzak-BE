package com.napzak.domain.store.core;

import com.napzak.domain.store.core.entity.GenrePreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenrePreferenceRepository extends JpaRepository<GenrePreferenceEntity, Long> {

   boolean existsByStoreEntityId(Long storeId);
}
