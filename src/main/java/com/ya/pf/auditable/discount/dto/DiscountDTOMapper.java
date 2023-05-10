package com.ya.pf.auditable.discount.dto;

import com.ya.pf.auditable.discount.DiscountEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DiscountDTOMapper implements Function<DiscountEntity, DiscountDTO> {

    @Override
    public DiscountDTO apply(DiscountEntity customerDiscount) {

        return new DiscountDTO(customerDiscount.getId(),
                customerDiscount.getDiscount(),
                customerDiscount.getCustomerEntity(),
                customerDiscount.getProductEntity());
    }

}
