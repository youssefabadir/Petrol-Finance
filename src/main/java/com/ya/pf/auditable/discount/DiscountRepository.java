package com.ya.pf.auditable.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {

    DiscountEntity findByCustomerEntity_IdAndProductEntity_Id(long customerId, long productId);

}
