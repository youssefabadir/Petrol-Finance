package com.ya.pf.auditable.discount.view;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountViewRepository extends JpaRepository<DiscountView, Long> {

    Page<DiscountView> findByCustomerNameContainingAndProductNameContaining(String customerName, String productName, Pageable pageable);

    Page<DiscountView> findByCustomerNameContaining(String customerName, Pageable pageable);

    Page<DiscountView> findByProductNameContaining(String productName, Pageable pageable);

}
