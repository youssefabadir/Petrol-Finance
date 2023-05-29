package com.ya.pf.auditable.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    void deleteByPaymentId(long paymentId);

    void deleteByBillId(long billId);


}
