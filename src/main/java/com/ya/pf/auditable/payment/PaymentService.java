package com.ya.pf.auditable.payment;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingRequestValueException;

import java.time.LocalDate;

public interface PaymentService {

    PaymentEntity validatePayment(PaymentEntity payment) throws MissingRequestValueException;

    Page<PaymentEntity> getPayments(long paymentMethodId, int pageNo, int pageSize, String sortBy, String order, LocalDate start, LocalDate end);

    void deletePaymentById(long id);

}
