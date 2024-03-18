package com.ya.pf.auditable.transaction.customer_transaction.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CustomerTransactionRepository extends JpaRepository<CustomerTransactionEntity, Long> {

    @Modifying
    @Query("UPDATE CustomerTransactionEntity t SET t.customerBalance = t.customerBalance + :amount " +
            "WHERE t.customerId = :customerId AND (t.date > :date OR (t.date = :date AND t.billId > :billId))")
    void updateCustomerBalanceByBillId(@Param("customerId") long customerId, @Param("billId") long billId, @Param("amount") float amount, @Param("date") Date date);

    @Modifying
    @Query("UPDATE CustomerTransactionEntity t SET t.customerBalance = t.customerBalance + :amount " +
            "WHERE t.customerId = :customerId AND (t.date > :date OR (t.date = :date AND t.paymentId > :paymentId))")
    void updateCustomerBalanceByPaymentId(@Param("customerId") long customerId, @Param("paymentId") long paymentId, @Param("amount") float amount, @Param("date") Date date);

    CustomerTransactionEntity findByBillId(long billId);

    CustomerTransactionEntity findByPaymentId(long paymentId);

    void deleteByBillId(long billId);

    void deleteByPaymentId(long paymentId);

    CustomerTransactionEntity findFirstByCustomerIdAndDateLessThanEqualOrderByDateDescIdDesc(long customerId, Date date);

    @Modifying
    @Query("UPDATE CustomerTransactionEntity c SET c.customerBalance = c.customerBalance + :amount WHERE c.customerId = :customerId AND c.date > :date")
    void updateCustomerBalanceByCustomerIdAfterDate(@Param("amount") float amount, @Param("customerId") long customerId, @Param("date") Date date);

    CustomerTransactionEntity findFirstByOrderByDateDescIdDesc();

}
