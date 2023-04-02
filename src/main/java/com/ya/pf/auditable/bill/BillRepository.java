package com.ya.pf.auditable.bill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {

	Page<BillEntity> findByReceiptNumberContaining(String receiptNumber, Pageable pageable);

	Page<BillEntity> findByCustomerEntity_Id(long id, Pageable pageable);

	Page<BillEntity> findByCustomerEntity_IdAndReceiptNumberContaining(long id, String receiptNumber, Pageable pageable);

	Page<BillEntity> findByBillDateBetween(Date start, Date end, Pageable pageable);

	Page<BillEntity> findByReceiptNumberContainingAndBillDateBetween(String receiptNumber, Date start, Date end, Pageable pageable);

	Page<BillEntity> findByCustomerEntity_IdAndBillDateBetween(long id, Date start, Date end, Pageable pageable);

	Page<BillEntity> findByCustomerEntity_IdAndReceiptNumberContainingAndBillDateBetween(long id, String receiptNumber, Date start, Date end, Pageable pageable);

}
