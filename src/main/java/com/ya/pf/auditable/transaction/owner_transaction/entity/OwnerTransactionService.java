package com.ya.pf.auditable.transaction.owner_transaction.entity;

import java.util.Date;

public interface OwnerTransactionService {

    void createOwnerTransaction(long supplierId, double amount, Long paymentId, Long billId, Date date);

    void deleteOwnerTransactionByBillId(long billId, double billAmount);

    void deleteOwnerTransactionByPaymentId(long paymentId, double paymentAmount);

}
