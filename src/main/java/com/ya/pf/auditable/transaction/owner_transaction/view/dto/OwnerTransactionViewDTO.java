package com.ya.pf.auditable.transaction.owner_transaction.view.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record OwnerTransactionViewDTO(
        String supplierName,
        Double supplierBalance,
        String paymentNumber,
        Double paymentAmount,
        Boolean transferredPayment,
        String paymentMethodName,
        String billNumber,
        Double billQuantity,
        Double billAmount,
        String productName,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date date
) {

}
