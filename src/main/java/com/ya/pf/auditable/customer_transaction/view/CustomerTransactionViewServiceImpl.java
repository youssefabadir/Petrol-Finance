package com.ya.pf.auditable.customer_transaction.view;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerTransactionViewServiceImpl implements CustomerTransactionViewService {

    private final CustomerTransactionViewRepository customerTransactionViewRepository;

    @Override
    public Page<CustomerTransactionView> getCustomerTransaction(long customerId, int pageNo, int pageSize, String sortBy,
                                                                String order, LocalDate start, LocalDate end) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);
        if (start == null || end == null) {
            return customerTransactionViewRepository.findAllByCustomerId(customerId, pageable);
        } else {
            return customerTransactionViewRepository.findByCustomerIdAndDateBetween(customerId, Date.valueOf(start),
                    Date.valueOf(end.plusDays(1)), pageable);
        }
    }

}
