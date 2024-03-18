package com.ya.pf.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Helper {

    public static Pageable preparePageable(int pageNo, int pageSize, String sortBy, String order) {

        return preparePageable(pageNo, pageSize, sortBy, order, "");
    }

    public static Pageable preparePageable(int pageNo, int pageSize, String sortBy, String order, String column) {

        Sort sort = Sort.by(sortBy);
        if (!order.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).descending();
        }
        if (!column.isBlank()) {
            if (order.equalsIgnoreCase("asc")) {
                sort = Sort.by(Sort.Order.asc(sortBy), Sort.Order.asc(column));
            } else {
                sort = Sort.by(Sort.Order.desc(sortBy), Sort.Order.desc(column));
            }
        }
        return PageRequest.of(pageNo, pageSize, sort);
    }

}
