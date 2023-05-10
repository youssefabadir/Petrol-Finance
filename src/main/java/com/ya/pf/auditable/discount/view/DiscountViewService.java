package com.ya.pf.auditable.discount.view;

import org.springframework.data.domain.Page;

public interface DiscountViewService {

    Page<DiscountView> getDiscounts(String customerName, String productName, int pageNo, int pageSize, String sortBy, String order);

}
