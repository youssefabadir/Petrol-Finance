package com.ya.pf.auditable.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CustomerReport {

	private final Page<TransactionEntity> transactionPage;

	private final double totalDueMoney;

	private final double totalPaidMoney;

}
