package com.ya.pf.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

public interface TransactionService {

    Page<TransactionEntity> getTransactions(String receiptNumber, int pageNo, int pageSize, String sortBy, String order);

    TransactionEntity createTransaction(TransactionEntity transactionEntity);

    TransactionEntity updateTransaction(TransactionEntity transactionEntity);

    void deleteTransactionById(long id);

    String getCustomerReport(long id, int pageNo, int pageSize) throws JsonProcessingException;

    Double getColumnSum(long id, String columnName);

}
