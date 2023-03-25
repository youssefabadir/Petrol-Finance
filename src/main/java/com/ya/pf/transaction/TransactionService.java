package com.ya.pf.transaction;

import org.springframework.data.domain.Page;

public interface TransactionService {

    Page<TransactionEntity> getTransactions(String receiptNumber, int pageNo, int pageSize, String sortBy, String order);

    TransactionEntity createTransaction(TransactionEntity transactionEntity);

    TransactionEntity updateTransaction(TransactionEntity transactionEntity);

    void deleteTransactionById(long id);

    String getCustomerReport(long id, String receiptNumber, int pageNo, int pageSize, String sortBy, String order);

    Double getColumnSum(long id, String columnName);

}
