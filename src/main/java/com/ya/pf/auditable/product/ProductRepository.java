package com.ya.pf.auditable.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	Page<ProductEntity> findByIsDeletedFalse(Pageable pageable);

	Page<ProductEntity> findByNameContainingAndIsDeletedFalse(String name, Pageable pageable);

}
