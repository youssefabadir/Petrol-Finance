package com.ya.pf.auditable.customer_transaction.view;

import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface CustomerTransactionViewService {

    Page<CustomerTransactionView> getCustomerTransaction(long customerId, int pageNo, int pageSize, String sortBy, String order, LocalDate start, LocalDate end);

}
