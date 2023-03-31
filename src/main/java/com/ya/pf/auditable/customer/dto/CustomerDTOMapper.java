package com.ya.pf.auditable.customer.dto;

import com.ya.pf.auditable.customer.CustomerEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerDTOMapper implements Function<CustomerEntity, CustomerDTO> {

    @Override
    public CustomerDTO apply(CustomerEntity customerEntity) {

        return new CustomerDTO(customerEntity.getId(), customerEntity.getName());
    }

}
