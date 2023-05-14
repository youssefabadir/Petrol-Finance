package com.ya.pf.auditable.customer_payment.dto;

import com.ya.pf.auditable.customer.dto.CustomerDTOMapper;
import com.ya.pf.auditable.customer_payment.CustomerPaymentEntity;
import com.ya.pf.auditable.payment_method.dto.PaymentMethodDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerPaymentDTOMapper implements Function<CustomerPaymentEntity, CustomerPaymentDTO> {

    private final CustomerDTOMapper customerDTOMapper;

    private final PaymentMethodDTOMapper paymentMethodDTOMapper;

    @Override
    public CustomerPaymentDTO apply(CustomerPaymentEntity customerPaymentEntity) {

        return new CustomerPaymentDTO(customerPaymentEntity.getId(),
                customerDTOMapper.apply(customerPaymentEntity.getCustomerEntity()),
                paymentMethodDTOMapper.apply(customerPaymentEntity.getPaymentMethodEntity()),
                customerPaymentEntity.getNumber(),
                customerPaymentEntity.getAmount(),
                customerPaymentEntity.isTransferred(),
                customerPaymentEntity.getDate());
    }

}
