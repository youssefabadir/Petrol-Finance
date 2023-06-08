package com.ya.pf.auditable.transaction.owner_transaction.view;

import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface OwnerTransactionViewService {

    Page<OwnerTransactionView> getSupplierTransaction(long supplierId, int pageNo, int pageSize, String sortBy, String order, LocalDate start, LocalDate end);

}
