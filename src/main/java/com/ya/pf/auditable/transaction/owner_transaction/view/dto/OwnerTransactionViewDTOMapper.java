package com.ya.pf.auditable.transaction.owner_transaction.view.dto;

import com.ya.pf.auditable.transaction.owner_transaction.view.OwnerTransactionView;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OwnerTransactionViewDTOMapper implements Function<OwnerTransactionView, OwnerTransactionViewDTO> {

    @Override
    public OwnerTransactionViewDTO apply(OwnerTransactionView ownerTransactionView) {

        return new OwnerTransactionViewDTO(ownerTransactionView.getSupplierName(),
                                           ownerTransactionView.getOwnerSupplierBalance(),
                                           ownerTransactionView.getPaymentNumber(),
                                           ownerTransactionView.getPaymentAmount(),
                                           ownerTransactionView.getTransferredPayment(),
                                           ownerTransactionView.getPaymentMethodName(),
                                           ownerTransactionView.getBillNumber(),
                                           ownerTransactionView.getBillQuantity(),
                                           ownerTransactionView.getBillAmount(),
                                           ownerTransactionView.getProductName(),
                                           ownerTransactionView.getDate());
    }

}
