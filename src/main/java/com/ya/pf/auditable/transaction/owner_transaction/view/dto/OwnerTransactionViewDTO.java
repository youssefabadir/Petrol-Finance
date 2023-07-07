package com.ya.pf.auditable.transaction.owner_transaction.view.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record OwnerTransactionViewDTO(
        String supplierName,
        Float supplierBalance,
        String paymentNumber,
        Float paymentAmount,
        Boolean transferredPayment,
        String paymentMethodName,
        String billNumber,
        Float billQuantity,
        Float billAmount,
        String productName,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date date
) {

}
