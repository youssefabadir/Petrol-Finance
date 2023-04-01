package com.ya.pf.auditable.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

	Page<TransactionEntity> findByIsDeletedFalse(Pageable pageable);

	Page<TransactionEntity> findByReceiptNumberContainingAndIsDeletedFalse(String receiptNumber, Pageable pageable);

	Page<TransactionEntity> findByCustomerEntity_IdAndIsDeletedFalse(long id, Pageable pageable);

	Page<TransactionEntity> findByCustomerEntity_IdAndReceiptNumberContainingAndIsDeletedFalse(long id, String receiptNumber, Pageable pageable);

	Page<TransactionEntity> findByTransactionDateBetweenAndIsDeletedFalse(Date start, Date end, Pageable pageable);

	Page<TransactionEntity> findByReceiptNumberContainingAndTransactionDateBetweenAndIsDeletedFalse(String receiptNumber, Date start, Date end, Pageable pageable);

	Page<TransactionEntity> findByCustomerEntity_IdAndTransactionDateBetweenAndIsDeletedFalse(long id, Date start, Date end, Pageable pageable);

	Page<TransactionEntity> findByCustomerEntity_IdAndReceiptNumberContainingAndTransactionDateBetweenAndIsDeletedFalse(long id, String receiptNumber, Date start, Date end, Pageable pageable);

}
