package com.ya.pf.auditable.payment.customer_payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record CustomerPaymentDTO(
        Long id,
        String number,
        Double amount,
        Long paymentMethodId,
        Long customerId,
        Boolean transferred,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date date
) {

}
