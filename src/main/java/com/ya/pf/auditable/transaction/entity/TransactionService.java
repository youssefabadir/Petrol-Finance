package com.ya.pf.auditable.transaction.entity;

import java.util.Date;

public interface TransactionService {

	void createTransaction(long customerId, long supplierId, double amount, Long paymentId, Long billId, Date date);

	double getPreviousBalance(long customerId);

	void deleteTransactionByPaymentId(long paymentId, double paymentAmount);

	void deleteTransactionByBillId(long billId, double billAmount);

}
