package com.ya.pf.auditable.transaction.customer_transaction.entity;

import java.util.Date;

public interface CustomerTransactionService {

    void createCustomerTransaction(long customerId, double amount, Long paymentId, Long billId, Date date);

    void deleteCustomerTransactionByBillId(long billId, double billAmount);

    void deleteCustomerTransactionByPaymentId(long paymentId, double paymentAmount);

}
