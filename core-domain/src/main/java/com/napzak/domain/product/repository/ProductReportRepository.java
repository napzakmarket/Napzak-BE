package com.napzak.domain.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.napzak.domain.product.entity.ProductReportEntity;

@Repository
public interface ProductReportRepository extends JpaRepository<ProductReportEntity, Long> {

	@Query("SELECT p.reportedProductImages FROM ProductReportEntity p")
	List<String> findAllReportedProductImages();

}