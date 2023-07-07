package com.ya.pf.auditable.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Page<CustomerEntity> findByNameContaining(String name, Pageable pageable);

    List<CustomerEntity> findByNameContaining(String name);

    @Modifying
    @Query("update CustomerEntity c set c.balance = :customerBalance where c.id = :customerId")
    void updateCustomerBalance(@Param("customerId") long customerId, @Param("customerBalance") float customerBalance);

}
