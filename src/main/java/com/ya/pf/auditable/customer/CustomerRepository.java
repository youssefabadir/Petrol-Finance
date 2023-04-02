package com.ya.pf.auditable.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

	Page<CustomerEntity> findByNameContaining(String name, Pageable pageable);

	List<CustomerEntity> findByNameContaining(String name);

}
