package com.ya.pf.auditable.customer_transaction.entity;

import java.util.Date;

public interface CustomerTransactionService {

    void createCustomerTransaction(long customerId, double amount, Long paymentId, Long billId, Date date);

    void deleteCustomerTransactionByPaymentId(long paymentId, double paymentAmount);

    void deleteCustomerTransactionByBillId(long billId, double billAmount);

}
