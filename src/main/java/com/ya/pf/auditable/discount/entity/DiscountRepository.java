package com.ya.pf.auditable.discount.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {

    DiscountEntity findByCustomerIdAndProductId(long customerId, long productId);

}
