package com.ya.pf.auditable.transaction;

public interface TransactionService {

    void deleteTransactionByPaymentId(long paymentId, double paymentAmount);

    void deleteTransactionByBillId(long billId, double billAmount);

}
