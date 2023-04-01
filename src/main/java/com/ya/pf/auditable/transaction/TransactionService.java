package com.ya.pf.auditable.transaction;

import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface TransactionService {

	Page<TransactionEntity> getTransactions(String receiptNumber, int pageNo, int pageSize, String sortBy, String order, LocalDate start, LocalDate end);

	TransactionEntity createTransaction(TransactionEntity transactionEntity);

	TransactionEntity updateTransaction(TransactionEntity transactionEntity);

	void deleteTransactionById(long id);

	CustomerReport getCustomerReport(long id, String receiptNumber, int pageNo, int pageSize, String sortBy, String order, LocalDate start, LocalDate end);

	Double getColumnSum(long id, String columnName, LocalDate start, LocalDate end);

}
