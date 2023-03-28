package com.ya.pf.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Helper {

	public static Pageable preparePageable(int pageNo, int pageSize) {

        return preparePageable(pageNo, pageSize, "id", "desc");
    }

    public static Pageable preparePageable(int pageNo, int pageSize, String sortBy, String order) {

        if (order.equalsIgnoreCase("asc")) {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        } else {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
    }

}
