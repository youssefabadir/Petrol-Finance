package com.ya.pf.auditable.payment.owner_payment;

import com.ya.pf.auditable.payment.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.MissingRequestValueException;

public interface OwnerPaymentService {

    Page<OwnerPaymentEntity> getOwnerPayments(String number, int pageNo, int pageSize, String sortBy, String order);

    OwnerPaymentEntity createOwnerPayment(OwnerPaymentEntity ownerPayment) throws MissingRequestValueException;

    OwnerPaymentEntity updateOwnerPayment(OwnerPaymentEntity ownerPayment) throws MissingRequestValueException;

    void createOwnerTransferredPayment(PaymentEntity payment, long supplierId) throws MissingRequestValueException;

}
