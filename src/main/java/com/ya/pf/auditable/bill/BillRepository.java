package com.ya.pf.auditable.bill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {

	Page<BillEntity> findByNumberContaining(String number, Pageable pageable);

	Page<BillEntity> findByDateBetween(Date start, Date end, Pageable pageable);

	Page<BillEntity> findByNumberContainingAndDateBetween(String number, Date start, Date end, Pageable pageable);

	boolean existsByNumberAndSupplierEntity_Id(String number, long id);

}
