package com.ya.pf.auditable.transaction.dto;

import com.ya.pf.auditable.transaction.BillEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BillDTOMapper implements Function<BillEntity, BillDTO> {

	@Override
	public BillDTO apply(BillEntity billEntity) {

		return new BillDTO(
				billEntity.getId(),
				billEntity.getSupplierEntity(),
				billEntity.getCustomerEntity(),
				billEntity.getProductEntity(),
				billEntity.getReceiptNumber(),
				billEntity.getAmount(),
				billEntity.getDueMoney(),
				billEntity.getPaidMoney(),
				billEntity.getTransactionDate()
		);
	}

}
