package com.ya.pf.auditable.payment;

import org.springframework.web.bind.MissingRequestValueException;

public interface PaymentService {

    PaymentEntity validatePayment(PaymentEntity payment) throws MissingRequestValueException;

    void deletePaymentById(long id);

}
