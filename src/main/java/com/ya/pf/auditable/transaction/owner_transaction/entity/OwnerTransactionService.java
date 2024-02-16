package com.ya.pf.auditable.transaction.owner_transaction.entity;

import java.util.Date;

public interface OwnerTransactionService {

    void createOwnerTransaction(Long paymentId, Date date);

    void createOwnerTransaction(long supplierId, float amount, Long paymentId, Long billId, Date date);

    void deleteOwnerTransactionByBillId(long supplierId, long billId, float billAmount, Date date);

    void deleteOwnerTransactionByPaymentId(long supplierId, long paymentId, float paymentAmount, Date date);

}
