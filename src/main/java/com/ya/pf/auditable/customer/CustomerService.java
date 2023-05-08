package com.ya.pf.auditable.customer;

import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

	Page<CustomerEntity> getCustomers(String name, int pageNo, int pageSize, String sortBy, String order);

	CustomerEntity createCustomer(CustomerEntity customerEntity);

	CustomerEntity updateCustomer(CustomerEntity customerEntity);

	void deleteCustomer(long id);

	List<CustomerEntity> searchCustomer(String name);

	CustomerEntity getCustomerById(long id);

	void updateCustomerBalance(long customerId, double customerBalance);

}
