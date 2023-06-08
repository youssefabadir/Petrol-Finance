package com.ya.pf.auditable.payment_method;

import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentMethodService {

    Page<PaymentMethodEntity> getPaymentMethods(String name, int pageNo, int pageSize, String sortBy, String order);

    PaymentMethodEntity createPaymentMethod(PaymentMethodEntity paymentMethodEntity);

    PaymentMethodEntity updatePaymentMethod(PaymentMethodEntity paymentMethodEntity);

    void deletePaymentMethod(long id);

    List<PaymentMethodEntity> searchPaymentMethod(String name);

    PaymentMethodEntity getPaymentMethodById(long id);

    void updatePaymentMethodBalance(long id, double balance);

}
