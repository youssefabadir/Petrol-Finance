package com.ya.pf.auditable.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

	Page<CustomerEntity> findByIsDeletedFalse(Pageable pageable);

	Page<CustomerEntity> findByNameContainingAndIsDeletedFalse(String name, Pageable pageable);

	List<CustomerEntity> findByNameContainingAndIsDeletedFalse(String name);

}
