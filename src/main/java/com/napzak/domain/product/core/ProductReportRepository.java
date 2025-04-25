package com.napzak.domain.product.core;

import org.springframework.data.jpa.repository.JpaRepository;

import com.napzak.domain.product.core.entity.ProductReportEntity;

public interface ProductReportRepository extends JpaRepository<ProductReportEntity, Long> {
}
