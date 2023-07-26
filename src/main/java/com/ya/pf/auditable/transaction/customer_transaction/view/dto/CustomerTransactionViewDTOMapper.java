package com.ya.pf.auditable.transaction.customer_transaction.view.dto;

import com.ya.pf.auditable.transaction.customer_transaction.view.CustomerTransactionView;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerTransactionViewDTOMapper implements Function<CustomerTransactionView, CustomerTransactionViewDTO> {

    @Override
    public CustomerTransactionViewDTO apply(CustomerTransactionView customerTransactionView) {

        return new CustomerTransactionViewDTO(customerTransactionView.getCustomerName(),
                                              customerTransactionView.getCustomerBalance(),
                                              customerTransactionView.getPaymentNumber(),
                                              customerTransactionView.getPaymentAmount(),
                                              customerTransactionView.getTransferredPayment(),
                                              customerTransactionView.getPaymentMethodName(),
                                              customerTransactionView.getBillNumber(),
                                              customerTransactionView.getBillQuantity(),
                                              customerTransactionView.getBillCustomerAmount(),
                                              customerTransactionView.getProductName(),
                                              customerTransactionView.getDate());
    }

}
