package com.napzak.domain.external.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.napzak.domain.external.core.entity.UseTermsEntity;
import com.napzak.domain.external.core.entity.enums.TermsType;

@Repository
public interface UseTermsRepository extends JpaRepository<UseTermsEntity, Long> {
	UseTermsEntity findByTermsTitleAndBundleId(TermsType termsType, int bundleId);

}
