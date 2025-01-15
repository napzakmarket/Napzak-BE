package com.napzak.domain.interest.core;

import com.napzak.domain.interest.core.entity.InterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InterestRepository extends JpaRepository<InterestEntity, Long> {

    boolean existsByStoreEntityIdAndProductEntityId(Long storeId, Long productId);

}