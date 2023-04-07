package com.ya.pf.auditable.transaction.view;

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
public class TransactionViewServiceImpl implements TransactionViewService {

	private final TransactionViewRepository transactionViewRepository;

	@Override
	public Page<TransactionView> getCustomerTransaction(long customerId, int pageNo, int pageSize, String sortBy,
	                                                    String order, LocalDate start, LocalDate end) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);
		if (start == null || end == null) {
			return transactionViewRepository.findAll(pageable);
		} else {
			return transactionViewRepository.findByDateBetween(Date.valueOf(start), Date.valueOf(end), pageable);
		}
	}

}
