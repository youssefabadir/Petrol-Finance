package com.ya.pf.auditable.payment.owner_payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.supplier.dto.SupplierDTO;

import java.util.Date;

public record OwnerPaymentDTO(
        Long id,
        String number,
        Float amount,
        String paymentMethodName,
        SupplierDTO supplier,
        Boolean transferred,
        String note,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date date
) {

}
