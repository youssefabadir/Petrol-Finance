package com.ya.pf.auditable.customer_payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPaymentRepository extends JpaRepository<CustomerPaymentEntity, Long> {

	Page<CustomerPaymentEntity> findByNumberContaining(String number, Pageable pageable);

	boolean existsByNumberAndPaymentMethodEntity_Id(String number, long id);

	CustomerPaymentEntity getByNumber(String number);

}
