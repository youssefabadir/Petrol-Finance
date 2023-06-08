package com.ya.pf.auditable.transaction.customer_transaction.view;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CustomerTransactionViewRepository extends JpaRepository<CustomerTransactionView, Long> {

    Page<CustomerTransactionView> findAllByCustomerId(long customerId, Pageable pageable);

    Page<CustomerTransactionView> findByCustomerIdAndDateBetween(long customerId, Date start, Date end, Pageable pageable);

}
