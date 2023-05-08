package com.ya.pf.auditable.discount.dto;

import com.ya.pf.auditable.customer.CustomerEntity;
import com.ya.pf.auditable.product.ProductEntity;

public record CustomerDiscountDTO(long id, double discount, CustomerEntity customer, ProductEntity product) {

}
