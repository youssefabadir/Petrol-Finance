package com.ya.pf.auditable.payment.owner_payment;

import com.ya.pf.auditable.payment.PaymentEntity;
import org.springframework.data.domain.Page;

public interface OwnerPaymentService {

    Page<OwnerPaymentEntity> getOwnerPayments(String number, int pageNo, int pageSize, String sortBy, String order);

    OwnerPaymentEntity createOwnerPayment(OwnerPaymentEntity ownerPayment);

    OwnerPaymentEntity updateOwnerPayment(OwnerPaymentEntity ownerPayment);

    void createOwnerTransferredPayment(PaymentEntity payment, long supplierId);

}
