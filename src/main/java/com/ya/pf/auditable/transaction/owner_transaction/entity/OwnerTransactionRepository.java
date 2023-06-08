package com.ya.pf.auditable.transaction.owner_transaction.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerTransactionRepository extends JpaRepository<OwnerTransactionEntity, Long> {

    @Modifying
    @Query("UPDATE OwnerTransactionEntity t SET t.supplierBalance = t.supplierBalance + :amount WHERE t.billId > :billId")
    void updateSupplierBalanceByBillId(@Param("billId") long billId, @Param("amount") double amount);

    @Modifying
    @Query("UPDATE OwnerTransactionEntity t SET t.supplierBalance = t.supplierBalance + :amount WHERE t.paymentId > :paymentId")
    void updateSupplierBalanceByPaymentId(@Param("paymentId") long paymentId, @Param("amount") double amount);

    OwnerTransactionEntity findByBillId(long billId);

    OwnerTransactionEntity findByPaymentId(long paymentId);

}
