package com.ya.pf.auditable.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.customer.dto.CustomerDTO;
import com.ya.pf.auditable.supplier.dto.SupplierDTO;

import java.util.Date;

public record PaymentDTO(
        String paymentNumber,
        double paymentAmount,
        long paymentMethodId,
        String paymentMethodName,
        double paymentMethodBalance,
        double treasuryBalance,
        CustomerDTO customer,
        SupplierDTO supplier,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date date
) {

}
