package com.ya.pf.auditable.discount;

import org.springframework.data.domain.Page;

public interface CustomerDiscountService {

	Page<CustomerDiscountEntity> getCustomersDiscount(int pageNo, int pageSize, String sortBy, String order);

	CustomerDiscountEntity createCustomerDiscount(CustomerDiscountEntity customerDiscount);

	CustomerDiscountEntity updateCustomerDiscount(CustomerDiscountEntity customerDiscount);

	void deleteCustomerDiscount(long id);

	double getCustomerDiscountForProduct(long customerId, long productId);

}
