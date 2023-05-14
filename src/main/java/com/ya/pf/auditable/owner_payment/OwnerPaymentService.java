package com.ya.pf.auditable.owner_payment;

import org.springframework.data.domain.Page;

public interface OwnerPaymentService {

    Page<OwnerPaymentEntity> getOwnerPayments(String number, int pageNo, int pageSize, String sortBy, String order);

    OwnerPaymentEntity createOwnerPayment(OwnerPaymentEntity ownerPaymentEntity);

    void deleteOwnerPayment(long id);

    void deleteTransferredOwnerPayment(String number, long paymentMethodId);

}
