package com.ya.pf.auditable.payment.owner_payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerPaymentRepository extends JpaRepository<OwnerPaymentEntity, Long> {

    Page<OwnerPaymentEntity> findByNumberContaining(String number, Pageable pageable);

    boolean existsByNumberAndPaymentMethodId(String number, long paymentMethodId);

}
