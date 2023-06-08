package com.ya.pf.auditable.transaction.owner_transaction.view;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OwnerTransactionViewRepository extends JpaRepository<OwnerTransactionView, Long> {

    Page<OwnerTransactionView> findAllBySupplierId(long customerId, Pageable pageable);

    Page<OwnerTransactionView> findBySupplierIdAndDateBetween(long customerId, Date start, Date end, Pageable pageable);

}
