package com.ya.pf.auditable.customer_payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.customer.dto.CustomerDTO;
import com.ya.pf.auditable.payment_method.dto.PaymentMethodDTO;

import java.util.Date;

public record CustomerPaymentDTO(
		long id,
		CustomerDTO customer,
		PaymentMethodDTO paymentMethodDTO,
		String number,
		double amount,
		boolean transferred,
		@JsonFormat(pattern = "dd-MM-yyyy")
		Date date
) {

}
