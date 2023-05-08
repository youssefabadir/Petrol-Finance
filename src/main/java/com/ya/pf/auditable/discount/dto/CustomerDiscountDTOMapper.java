package com.ya.pf.auditable.discount.dto;

import com.ya.pf.auditable.discount.CustomerDiscountEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerDiscountDTOMapper implements Function<CustomerDiscountEntity, CustomerDiscountDTO> {

	@Override
	public CustomerDiscountDTO apply(CustomerDiscountEntity customerDiscount) {

		return new CustomerDiscountDTO(customerDiscount.getId(),
				customerDiscount.getDiscount(),
				customerDiscount.getCustomerEntity(),
				customerDiscount.getProductEntity());
	}

}
