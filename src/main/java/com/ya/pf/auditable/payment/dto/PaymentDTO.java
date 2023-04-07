package com.ya.pf.auditable.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.customer.dto.CustomerDTO;
import com.ya.pf.auditable.paymentMethod.dto.PaymentMethodDTO;
import com.ya.pf.auditable.supplier.dto.SupplierDTO;

import java.util.Date;

public record PaymentDTO(
		long id,
		CustomerDTO customer,
		SupplierDTO supplier,
		PaymentMethodDTO paymentMethodDTO,
		String number,
		double amount,
		@JsonFormat(pattern = "dd-MM-yyyy")
		Date date
) {

}
