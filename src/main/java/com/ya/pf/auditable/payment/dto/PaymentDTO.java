package com.ya.pf.auditable.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.customer.dto.CustomerDTO;
import com.ya.pf.auditable.wayofpayment.dto.WayOfPaymentDTO;

import java.util.Date;

public record PaymentDTO(
		long id,
		CustomerDTO customer,
		WayOfPaymentDTO wayOfPaymentDTO,
		String receiptNumber,
		double amount,
		@JsonFormat(pattern = "dd-MM-yyyy")
		Date paymentDate
) {

}
