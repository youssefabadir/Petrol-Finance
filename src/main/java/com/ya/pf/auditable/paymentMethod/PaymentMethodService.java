package com.ya.pf.auditable.paymentMethod;

import org.springframework.data.domain.Page;

public interface PaymentMethodService {

	Page<PaymentMethodEntity> getWayOfPayments(String name, int pageNo, int pageSize, String sortBy, String order);

	PaymentMethodEntity createWayOfPayment(PaymentMethodEntity paymentMethodEntity);

	PaymentMethodEntity updateWayOfPayment(PaymentMethodEntity paymentMethodEntity);

	void deleteWayOfPayment(long id);

}
