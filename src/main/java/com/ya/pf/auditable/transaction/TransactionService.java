package com.ya.pf.auditable.transaction;

public interface TransactionService {

    void deleteTransactionByPaymentId(long paymentId, float paymentAmount);

    void deleteTransactionByBillId(long billId, float billAmount);

}
