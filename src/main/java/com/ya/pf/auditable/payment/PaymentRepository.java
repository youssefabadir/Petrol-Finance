package com.ya.pf.auditable.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    boolean existsByNumberAndPaymentMethodIdAndPaymentTypeEquals(String number, long paymentMethodId, String paymentType);

    PaymentEntity findFirstByOrderByIdDesc();

    void deleteByNumberAndPaymentMethodIdAndTransferredIsTrue(String number, long paymentMethodId);

    PaymentEntity findByNumberEqualsAndIdNot(String number, long id);

}
