package com.ya.pf.auditable.customer_payment;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingRequestValueException;

public interface CustomerPaymentService {

    Page<CustomerPaymentEntity> getCustomerPayments(String number, int pageNo, int pageSize, String sortBy, String order);

    CustomerPaymentEntity createCustomerPayment(CustomerPaymentEntity customerPaymentEntity, boolean transferred, long supplierId) throws MissingRequestValueException;

    void deleteCustomerPayment(long paymentId);

    void deleteTransferredCustomerPayment(String number, long paymentMethodId);

}
