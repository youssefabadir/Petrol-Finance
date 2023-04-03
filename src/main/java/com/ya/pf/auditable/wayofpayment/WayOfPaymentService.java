package com.ya.pf.auditable.wayofpayment;

import org.springframework.data.domain.Page;

public interface WayOfPaymentService {

	Page<WayOfPaymentEntity> getWayOfPayments(String name, int pageNo, int pageSize, String sortBy, String order);

	WayOfPaymentEntity createWayOfPayment(WayOfPaymentEntity wayOfPaymentEntity);

	WayOfPaymentEntity updateWayOfPayment(WayOfPaymentEntity wayOfPaymentEntity);

	void deleteWayOfPayment(long id);

}
