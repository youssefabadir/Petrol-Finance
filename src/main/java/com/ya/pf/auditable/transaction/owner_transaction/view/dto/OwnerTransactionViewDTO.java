package com.ya.pf.auditable.transaction.owner_transaction.view.dto;

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
        Date date
) {

}
