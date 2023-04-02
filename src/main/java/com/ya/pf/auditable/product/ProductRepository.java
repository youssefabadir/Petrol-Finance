package com.ya.pf.auditable.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	Page<ProductEntity> findByNameContaining(String name, Pageable pageable);

	List<ProductEntity> findByNameContaining(String name);

}
