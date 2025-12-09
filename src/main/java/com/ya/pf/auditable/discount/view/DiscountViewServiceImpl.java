package com.ya.pf.auditable.discount.view;

import com.ya.pf.util.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountViewServiceImpl implements DiscountViewService {

    private final DiscountViewRepository discountViewRepository;

    @Override
    public Page<DiscountView> getDiscounts(String customerName, String productName, int pageNo, int pageSize,
                                           String sortBy, String order) {

        Pageable pageable = PageableHelper.preparePageable(pageNo, pageSize, sortBy, order);
        if (!customerName.isEmpty() && !productName.isEmpty()) {
            return discountViewRepository.findByCustomerNameContainingAndProductNameContaining(customerName,
                                                                                               productName,
                                                                                               pageable);
        } else if (!customerName.isEmpty()) {
            return discountViewRepository.findByCustomerNameContaining(customerName, pageable);
        } else if (!productName.isEmpty()) {
            return discountViewRepository.findByProductNameContaining(productName, pageable);
        } else {
            return discountViewRepository.findAll(pageable);
        }
    }

}
