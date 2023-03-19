package com.ya.pf.customer;

import org.springframework.data.domain.Page;

public interface CustomerService {

    Page<CustomerEntity> getCustomers(String name, int pageNo, int pageSize, String sortBy, String order);

    CustomerEntity createCustomer(CustomerEntity customerEntity);

    CustomerEntity updateCustomer(CustomerEntity customerEntity);

    void deleteCustomer(Long id);

}
