package com.ya.pf.auditable.bill;

import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface BillService {

    Page<BillEntity> getBills(String receiptNumber, int pageNo, int pageSize, String sortBy, String order, LocalDate start, LocalDate end);

    BillEntity createBill(BillEntity billEntity, long truckId);

    BillEntity updateBill(BillEntity billEntity, long truckId);

    void deleteBill(long id);

}
