package com.ya.pf.auditable.bill.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.customer.dto.CustomerDTO;
import com.ya.pf.auditable.product.dto.ProductDTO;
import com.ya.pf.auditable.supplier.dto.SupplierDTO;

import java.util.Date;

public record BillDTO(
		long id,
		SupplierDTO supplier,
		CustomerDTO customer,
		ProductDTO product,
		String receiptNumber,
		double amount,
		double dueMoney,
		double paidMoney,
		@JsonFormat(pattern = "dd-MM-yyyy")
		Date transactionDate
) {

}
