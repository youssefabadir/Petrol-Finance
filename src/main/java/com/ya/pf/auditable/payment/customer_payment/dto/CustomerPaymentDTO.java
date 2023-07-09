package com.ya.pf.auditable.payment.customer_payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.customer.dto.CustomerDTO;

import java.util.Date;

public record CustomerPaymentDTO(
        Long id,
        String number,
        Float amount,
        String paymentMethodName,
        CustomerDTO customer,
        Boolean transferred,
        String note,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date date
) {

}
