package com.ya.pf.auditable.transaction.view;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionViewRepository extends JpaRepository<TransactionView, Long> {

	Page<TransactionView> findByDateBetween(Date start, Date end, Pageable pageable);

}
