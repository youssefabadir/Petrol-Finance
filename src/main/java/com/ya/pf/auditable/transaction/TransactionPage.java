package com.ya.pf.auditable.transaction;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
public class TransactionPage extends PageImpl<TransactionEntity> {

	private double totalDueMoney;

	private double totalPaidMoney;

	public TransactionPage(List<TransactionEntity> content, Pageable pageable, long total) {

		super(content, pageable, total);
	}

	public TransactionPage(List<TransactionEntity> content) {

		super(content);
	}

	public TransactionPage(Page<TransactionEntity> page) {

		super(page.getContent(), page.getPageable(), page.getPageable().getOffset() + page.getTotalElements());
	}

}
