package com.ya.pf.auditable.transaction.dto;

import com.ya.pf.auditable.transaction.TransactionEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TransactionDTOMapper implements Function<TransactionEntity, TransactionDTO> {

	@Override
	public TransactionDTO apply(TransactionEntity transactionEntity) {

		return new TransactionDTO(
				transactionEntity.getId(),
				transactionEntity.getSupplierEntity(),
				transactionEntity.getCustomerEntity(),
				transactionEntity.getProductEntity(),
				transactionEntity.getReceiptNumber(),
				transactionEntity.getAmount(),
				transactionEntity.getDueMoney(),
				transactionEntity.getPaidMoney(),
				transactionEntity.getTransactionDate()
		);
	}

}
