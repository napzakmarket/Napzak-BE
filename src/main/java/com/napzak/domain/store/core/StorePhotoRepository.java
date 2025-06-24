package com.napzak.domain.store.core;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.napzak.domain.store.core.entity.StorePhotoEntity;
import com.napzak.domain.store.core.entity.enums.PhotoType;

public interface StorePhotoRepository extends JpaRepository<StorePhotoEntity, Long> {
	Optional<StorePhotoEntity> findByPhotoType(PhotoType photoType);

	@Query("SELECT s.photoUrl FROM StorePhotoEntity s")
	List<String> findAllPhotoUrl();
}
