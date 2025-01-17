package com.napzak.domain.product.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.napzak.domain.product.core.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {

}
