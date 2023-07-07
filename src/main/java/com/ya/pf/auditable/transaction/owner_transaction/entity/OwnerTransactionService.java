package com.ya.pf.auditable.transaction.owner_transaction.entity;

import java.util.Date;

public interface OwnerTransactionService {

    void createOwnerTransaction(long supplierId, float amount, Long paymentId, Long billId, Date date);

    void deleteOwnerTransactionByBillId(long billId, float billAmount);

    void deleteOwnerTransactionByPaymentId(long paymentId, float paymentAmount);

}
