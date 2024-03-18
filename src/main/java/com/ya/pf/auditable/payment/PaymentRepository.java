package com.ya.pf.auditable.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    boolean existsByNumberAndPaymentMethodIdAndPaymentTypeEquals(String number, long paymentMethodId, String paymentType);

    PaymentEntity findFirstByPaymentMethodIdAndDateLessThanEqualOrderByDateDescIdDesc(long paymentMethodId, Date date);

    void deleteByNumberAndPaymentMethodIdAndTransferredIsTrue(String number, long paymentMethodId);

    PaymentEntity findByNumberEqualsAndIdNot(String number, long id);

    Page<PaymentEntity> findByPaymentMethodId(long paymentMethodId, Pageable pageable);

    Page<PaymentEntity> findAllByDateBetween(Date start, Date end, Pageable pageable);

    Page<PaymentEntity> findByPaymentMethodIdAndDateBetween(long paymentMethodId, Date start, Date end, Pageable pageable);

    @Modifying
    @Query("UPDATE PaymentEntity p SET p.paymentMethodBalance = p.paymentMethodBalance + :amount WHERE p.paymentMethodId = :paymentMethodId AND p.date > :date")
    void updatePaymentMethodBalance(@Param("paymentMethodId") long paymentMethodId, @Param("amount") float amount, @Param("date") Date date);

    @Modifying
    @Query("UPDATE PaymentEntity p SET p.paymentMethodBalance = p.paymentMethodBalance + :amount " +
            "WHERE p.paymentMethodId = :paymentMethodId AND (p.date > :date OR (p.date = :date AND p.id > :id))")
    void updatePaymentMethodBalanceById(@Param("paymentMethodId") long paymentMethodId, @Param("id") long id, @Param("amount") float amount, @Param("date") Date date);
}
