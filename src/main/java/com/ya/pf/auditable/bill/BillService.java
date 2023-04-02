package com.ya.pf.auditable.bill;

import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface BillService {

	Page<BillEntity> getBills(String receiptNumber, int pageNo, int pageSize, String sortBy, String order, LocalDate start, LocalDate end);

	BillEntity createBill(BillEntity billEntity);

	BillEntity updateBill(BillEntity billEntity);

	void deleteBill(long id);

	CustomerReport getCustomerReport(long id, String receiptNumber, int pageNo, int pageSize, String sortBy, String order, LocalDate start, LocalDate end);

	Double getColumnSum(long id, String columnName, LocalDate start, LocalDate end);

}
