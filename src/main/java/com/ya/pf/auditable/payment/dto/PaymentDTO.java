package com.ya.pf.auditable.payment.dto;

import com.ya.pf.auditable.customer.dto.CustomerDTO;

public record PaymentDTO(
		long id,
		CustomerDTO customer,
		String receiptNumber,
		double amount
) {

}
