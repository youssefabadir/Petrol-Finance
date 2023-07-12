package com.ya.pf.auditable.transaction.customer_transaction.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTransactionRepository extends JpaRepository<CustomerTransactionEntity, Long> {

    @Modifying
    @Query("UPDATE CustomerTransactionEntity t SET t.customerBalance = t.customerBalance + :amount WHERE t.billId > :billId")
    void updateCustomerBalanceByBillId(@Param("billId") long billId, @Param("amount") float amount);

    @Modifying
    @Query("UPDATE CustomerTransactionEntity t SET t.customerBalance = t.customerBalance + :amount WHERE t.paymentId > :paymentId")
    void updateCustomerBalanceByPaymentId(@Param("paymentId") long paymentId, @Param("amount") float amount);

    CustomerTransactionEntity findByBillId(long billId);

    CustomerTransactionEntity findByPaymentId(long paymentId);

    void deleteByBillId(long billId);

    void deleteByPaymentId(long paymentId);

}
