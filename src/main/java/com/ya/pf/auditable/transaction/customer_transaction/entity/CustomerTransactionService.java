package com.ya.pf.auditable.transaction.customer_transaction.entity;

import java.util.Date;

public interface CustomerTransactionService {

    void createCustomerTransaction(Long paymentId, Date date);

    void createCustomerTransaction(long customerId, float amount, Long paymentId, Long billId, Date date);

    void deleteCustomerTransactionByBillId(long customerId, long billId, float billAmount, Date date);

    void deleteCustomerTransactionByPaymentId(long customerId, long paymentId, float paymentAmount, Date date);

}
