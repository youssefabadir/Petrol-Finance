package com.ya.pf.auditable.payment.customer_payment.dto;

import com.ya.pf.auditable.customer.dto.CustomerDTOMapper;
import com.ya.pf.auditable.payment.customer_payment.CustomerPaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerPaymentDTOMapper implements Function<CustomerPaymentEntity, CustomerPaymentDTO> {

    private final CustomerDTOMapper customerDTOMapper;

    @Override
    public CustomerPaymentDTO apply(CustomerPaymentEntity customerPaymentEntity) {

        return new CustomerPaymentDTO(customerPaymentEntity.getId(),
                                      customerPaymentEntity.getNumber(),
                                      customerPaymentEntity.getAmount(),
                                      customerPaymentEntity.getPaymentMethodName(),
                                      customerDTOMapper.apply(customerPaymentEntity.getCustomer()),
                                      customerPaymentEntity.isTransferred(),
                                      customerPaymentEntity.getNote(),
                                      customerPaymentEntity.getDate());
    }

}
