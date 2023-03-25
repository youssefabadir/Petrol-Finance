package com.ya.pf.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Page<TransactionEntity> findByReceiptNumberContaining(String receiptNumber, Pageable pageable);

    Page<TransactionEntity> findByCustomerEntity_IdAndReceiptNumberContaining(long id, String receiptNumber, Pageable pageable);

}
