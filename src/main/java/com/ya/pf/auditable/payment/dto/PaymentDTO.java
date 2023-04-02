package com.ya.pf.auditable.payment.dto;

import com.ya.pf.auditable.customer.CustomerEntity;

public record PaymentDTO(
		long id,
		CustomerEntity customer,
		String receiptNumber,
		double amount
) {

}
