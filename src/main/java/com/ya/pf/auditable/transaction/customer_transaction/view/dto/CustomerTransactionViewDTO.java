package com.ya.pf.auditable.transaction.customer_transaction.view.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record CustomerTransactionViewDTO(
        String customerName,
        Double customerBalance,
        String paymentNumber,
        Double paymentAmount,
        Boolean transferredPayment,
        String paymentMethod,
        String billNumber,
        Double billQuantity,
        Double billAmount,
        String productName,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date date
) {

}
