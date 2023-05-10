package com.ya.pf.auditable.discount.dto;

import com.ya.pf.auditable.customer.CustomerEntity;
import com.ya.pf.auditable.product.ProductEntity;

public record DiscountDTO(long id, double discount, CustomerEntity customer, ProductEntity product) {

}
