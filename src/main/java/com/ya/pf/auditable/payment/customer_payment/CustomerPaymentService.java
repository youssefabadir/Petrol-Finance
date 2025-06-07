package com.ya.pf.auditable.payment.customer_payment;

import org.springframework.data.domain.Page;

public interface CustomerPaymentService {

    Page<CustomerPaymentEntity> getCustomerPayments(String number, int pageNo, int pageSize, String sortBy, String order);

    CustomerPaymentEntity createCustomerPayment(CustomerPaymentEntity customerPayment, long supplierId);

    CustomerPaymentEntity updateCustomerPayment(CustomerPaymentEntity customerPayment, long supplierId);

}
