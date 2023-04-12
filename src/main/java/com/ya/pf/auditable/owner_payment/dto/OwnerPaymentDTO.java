package com.ya.pf.auditable.owner_payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.payment_method.dto.PaymentMethodDTO;
import com.ya.pf.auditable.supplier.dto.SupplierDTO;

import java.util.Date;

public record OwnerPaymentDTO(
		long id,
		String number,
		double amount,
		SupplierDTO supplier,
		PaymentMethodDTO paymentMethodDTO,
		boolean transferred,
		@JsonFormat(pattern = "dd-MM-yyyy")
		Date date
) {

}
