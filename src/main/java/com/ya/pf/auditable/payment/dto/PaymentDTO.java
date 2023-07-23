package com.ya.pf.auditable.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.customer.dto.CustomerDTO;
import com.ya.pf.auditable.supplier.dto.SupplierDTO;

import java.util.Date;

public record PaymentDTO(
        String paymentNumber,
        float paymentAmount,
        long paymentMethodId,
        String paymentMethodName,
        float paymentMethodBalance,
        float treasuryBalance,
        CustomerDTO customer,
        SupplierDTO supplier,
        String note,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date date
) {

}
