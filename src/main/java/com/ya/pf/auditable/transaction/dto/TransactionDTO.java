package com.ya.pf.auditable.transaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.customer.CustomerEntity;
import com.ya.pf.auditable.product.ProductEntity;
import com.ya.pf.auditable.supplier.SupplierEntity;

import java.util.Date;

public record TransactionDTO(
		long id,
		SupplierEntity supplier,
		CustomerEntity customer,
		ProductEntity product,
		String receiptNumber,
		double amount,
		double dueMoney,
		double paidMoney,
		@JsonFormat(pattern = "dd-MM-yyyy")
		Date transactionDate
) {

}
