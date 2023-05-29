package com.ya.pf.auditable.payment.owner_payment;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingRequestValueException;

public interface OwnerPaymentService {

    Page<OwnerPaymentEntity> getOwnerPayments(String number, int pageNo, int pageSize, String sortBy, String order);

    OwnerPaymentEntity createOwnerPayment(OwnerPaymentEntity ownerPayment) throws MissingRequestValueException;

}
