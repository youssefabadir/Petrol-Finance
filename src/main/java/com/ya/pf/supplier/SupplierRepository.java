package com.ya.pf.supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {

	Page<SupplierEntity> findByNameContaining(String name, Pageable pageable);

	List<SupplierEntity> findByNameContaining(String name);

}
