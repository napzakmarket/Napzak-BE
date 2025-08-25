package com.napzak.domain.external.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.napzak.domain.external.entity.LinkEntity;
import com.napzak.domain.external.entity.enums.LinkType;

@Repository
public interface LinkRepository extends JpaRepository<LinkEntity, Long> {

	LinkEntity findByType(LinkType linkType);

}
