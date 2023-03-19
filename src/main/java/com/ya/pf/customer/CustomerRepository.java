package com.ya.pf.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Page<CustomerEntity> findByNameContaining(String name, Pageable pageable);

}
