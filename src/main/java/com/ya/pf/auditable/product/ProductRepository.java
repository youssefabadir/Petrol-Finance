package com.ya.pf.auditable.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	Page<ProductEntity> findByIsDeletedFalse(Pageable pageable);

	Page<ProductEntity> findByNameContainingAndIsDeletedFalse(String name, Pageable pageable);

	List<ProductEntity> findByNameContainingAndIsDeletedFalse(String name);

}
