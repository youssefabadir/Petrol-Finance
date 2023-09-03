package com.ya.pf.auditable.payment.customer_payment;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingRequestValueException;

public interface CustomerPaymentService {

    Page<CustomerPaymentEntity> getCustomerPayments(String number, int pageNo, int pageSize, String sortBy, String order);

    CustomerPaymentEntity createCustomerPayment(CustomerPaymentEntity customerPayment, long supplierId) throws MissingRequestValueException;

    CustomerPaymentEntity updateCustomerPayment(CustomerPaymentEntity customerPayment, long supplierId) throws MissingRequestValueException;

}
