package com.ya.pf.auditable.payment.owner_payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record OwnerPaymentDTO(
        Long id,
        String number,
        Double amount,
        Long paymentMethodId,
        Long supplierId,
        Boolean transferred,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date date
) {

}
