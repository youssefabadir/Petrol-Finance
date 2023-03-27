package com.ya.pf.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Page<TransactionEntity> findByReceiptNumberContaining(String receiptNumber, Pageable pageable);

    Page<TransactionEntity> findByCustomerEntity_Id(long id, Pageable pageable);

    Page<TransactionEntity> findByCustomerEntity_IdAndReceiptNumberContaining(long id, String receiptNumber, Pageable pageable);

    Page<TransactionEntity> findByTransactionDateBetween(Date start, Date end, Pageable pageable);

    Page<TransactionEntity> findByReceiptNumberContainingAndTransactionDateBetween(String receiptNumber, Date start, Date end, Pageable pageable);

    Page<TransactionEntity> findByCustomerEntity_IdAndTransactionDateBetween(long id, Date start, Date end, Pageable pageable);

    Page<TransactionEntity> findByCustomerEntity_IdAndReceiptNumberContainingAndTransactionDateBetween(long id, String receiptNumber, Date start, Date end, Pageable pageable);

}
