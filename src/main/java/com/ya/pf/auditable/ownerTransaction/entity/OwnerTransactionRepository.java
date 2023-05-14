package com.ya.pf.auditable.ownerTransaction.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OwnerTransactionRepository extends JpaRepository<OwnerTransactionEntity, Long> {

    OwnerTransactionEntity findFirstBySupplierIdOrderByIdDesc(@Param("supplier_id") long supplierId);

    OwnerTransactionEntity findByOwnerPaymentId(long ownerPaymentId);

    void deleteByOwnerPaymentId(long id);

    void deleteByBillId(long id);

    @Modifying
    @Query("UPDATE OwnerTransactionEntity t SET t.ownerSupplierBalance = t.ownerSupplierBalance + :amount WHERE t.billId > :billId")
    void updateBillOwnerSupplierBalance(@Param("billId") long billId, @Param("amount") double amount);

    @Modifying
    @Query("UPDATE OwnerTransactionEntity t SET t.ownerSupplierBalance = t.ownerSupplierBalance + :amount WHERE t.ownerPaymentId > :paymentId")
    void updatePaymentOwnerSupplierBalance(@Param("paymentId") long paymentId, @Param("amount") double amount);

}
