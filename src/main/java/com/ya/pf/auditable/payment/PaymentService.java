package com.ya.pf.auditable.payment;

import org.springframework.data.domain.Page;

public interface PaymentService {

	Page<PaymentEntity> getPayments(String receiptNumber, int pageNo, int pageSize, String sortBy, String order);

	PaymentEntity createPayment(PaymentEntity paymentEntity);

	PaymentEntity updatePayment(PaymentEntity paymentEntity);

	void deletePayment(long id);

}
