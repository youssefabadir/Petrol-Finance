package com.ya.pf.auditable.transaction.customer_transaction.view.dto;

import java.util.Date;

public record CustomerTransactionViewDTO(
        String customerName,
        Double customerBalance,
        String paymentNumber,
        Double paymentAmount,
        Boolean transferredPayment,
        String paymentMethodName,
        String billNumber,
        Double billQuantity,
        Double billAmount,
        String productName,
        Date date
) {

}
