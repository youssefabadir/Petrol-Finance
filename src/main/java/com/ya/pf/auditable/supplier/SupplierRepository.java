package com.ya.pf.auditable.supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {

	Page<SupplierEntity> findByIsDeletedFalse(Pageable pageable);

	Page<SupplierEntity> findByNameContainingAndIsDeletedFalse(String name, Pageable pageable);

	List<SupplierEntity> findByNameContainingAndIsDeletedFalse(String name);

}
