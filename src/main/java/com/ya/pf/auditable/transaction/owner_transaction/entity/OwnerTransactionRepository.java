package com.ya.pf.auditable.transaction.owner_transaction.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OwnerTransactionRepository extends JpaRepository<OwnerTransactionEntity, Long> {

    @Modifying
    @Query("UPDATE OwnerTransactionEntity t SET t.supplierBalance = t.supplierBalance + :amount WHERE t.supplierId = :supplierId AND t.date > :date")
    void updateSupplierBalance(@Param("supplierId") long supplierId, @Param("amount") float amount, @Param("date") Date date);

    OwnerTransactionEntity findByBillId(long billId);

    OwnerTransactionEntity findByPaymentId(long paymentId);

    void deleteByBillId(long billId);

    void deleteByPaymentId(long paymentId);

    OwnerTransactionEntity findFirstBySupplierIdAndDateLessThanEqualOrderByIdDesc(long supplierId, Date date);

    @Modifying
    @Query("UPDATE OwnerTransactionEntity o SET o.supplierBalance = o.supplierBalance + :amount WHERE o.supplierId = :supplierId AND o.date > :date")
    void updateSupplierBalanceBySupplierIdAfterDate(@Param("amount") float amount, @Param("supplierId") long supplierId, @Param("date") Date date);

    OwnerTransactionEntity findFirstByOrderByDateDescIdDesc();

}
