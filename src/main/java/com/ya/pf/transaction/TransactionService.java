package com.ya.pf.transaction;

import org.springframework.data.domain.Page;

public interface TransactionService {

    Page<TransactionEntity> getTransactions(int receiptNumber, int pageNo, int pageSize, String sortBy, String order);

    TransactionEntity createTransaction(TransactionEntity transactionEntity);

    void updateTransaction(TransactionEntity transactionEntity);

    void deleteTransactionById(Long id);

}
