package com.ya.pf.auditable.ownerTransaction.entity;

import java.util.Date;

public interface OwnerTransactionService {

	void createOwnerTransaction(long supplierId, double amount, Long paymentId, Long billId, Date date);

	double getOwnerSupplierPreviousBalance(long supplierId);

	void deleteOwnerTransactionByPaymentId(long paymentId, double paymentAmount);

	void deleteOwnerTransactionByBillId(long billId, double billAmount);

}
