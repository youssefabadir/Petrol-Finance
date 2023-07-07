package com.ya.pf.auditable.transaction.customer_transaction.view.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record CustomerTransactionViewDTO(
        String customerName,
        Float customerBalance,
        String paymentNumber,
        Float paymentAmount,
        Boolean transferredPayment,
        String paymentMethod,
        String billNumber,
        Float billQuantity,
        Float billAmount,
        String productName,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date date
) {

}
