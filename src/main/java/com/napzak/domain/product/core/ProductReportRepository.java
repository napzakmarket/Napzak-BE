package com.napzak.domain.product.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.napzak.domain.product.core.entity.ProductReportEntity;

public interface ProductReportRepository extends JpaRepository<ProductReportEntity, Long> {

	@Query("SELECT p.reportedProductImages FROM ProductReportEntity p")
	List<String> findAllReportedProductImages();

}