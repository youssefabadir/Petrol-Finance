package com.ya.pf.auditable.transaction.view;

import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface TransactionViewService {

	Page<TransactionView> getCustomerTransaction(long customerId, int pageNo, int pageSize, String sortBy, String order, LocalDate start, LocalDate end);

}
