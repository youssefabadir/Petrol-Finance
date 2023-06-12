package com.ya.pf.auditable.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    boolean existsByNumberAndPaymentMethodIdAndPaymentTypeEquals(String number, long paymentMethodId, String paymentType);

    PaymentEntity findFirstByOrderByIdDesc();

    void deleteByNumberAndPaymentMethodIdAndTransferredIsTrue(String number, long paymentMethodId);

    PaymentEntity findByNumberEqualsAndIdNot(String number, long id);

    Page<PaymentEntity> findByPaymentMethodId(long paymentMethodId, Pageable pageable);

    Page<PaymentEntity> findAllByDateBetween(Date start, Date end, Pageable pageable);

    Page<PaymentEntity> findByPaymentMethodIdAndDateBetween(long paymentMethodId, Date start, Date end, Pageable pageable);

}
