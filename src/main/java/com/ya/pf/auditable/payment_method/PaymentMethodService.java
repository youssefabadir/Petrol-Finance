package com.ya.pf.auditable.payment_method;

import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentMethodService {

    Page<PaymentMethodEntity> getWayOfPayments(String name, int pageNo, int pageSize, String sortBy, String order);

    PaymentMethodEntity createWayOfPayment(PaymentMethodEntity paymentMethodEntity);

    PaymentMethodEntity updateWayOfPayment(PaymentMethodEntity paymentMethodEntity);

    void deleteWayOfPayment(long id);

    List<PaymentMethodEntity> searchPaymentMethod(String name);

}
