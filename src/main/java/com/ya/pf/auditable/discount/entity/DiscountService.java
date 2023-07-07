package com.ya.pf.auditable.discount.entity;

public interface DiscountService {

    DiscountEntity createDiscount(DiscountEntity customerDiscount);

    DiscountEntity updateDiscount(DiscountEntity customerDiscount);

    void deleteDiscount(long id);

    float getCustomerDiscountForProduct(long customerId, long productId);

}
