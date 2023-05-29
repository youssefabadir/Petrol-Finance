package com.ya.pf.auditable.payment.customer_payment.dto;

import com.ya.pf.auditable.payment.customer_payment.CustomerPaymentEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerPaymentDTOMapper implements Function<CustomerPaymentEntity, CustomerPaymentDTO> {

    @Override
    public CustomerPaymentDTO apply(CustomerPaymentEntity customerPaymentEntity) {

        return new CustomerPaymentDTO(customerPaymentEntity.getId(),
                                      customerPaymentEntity.getNumber(),
                                      customerPaymentEntity.getAmount(),
                                      customerPaymentEntity.getPaymentMethodId(),
                                      customerPaymentEntity.getCustomerId(),
                                      customerPaymentEntity.isTransferred(),
                                      customerPaymentEntity.getDate());
    }

}
