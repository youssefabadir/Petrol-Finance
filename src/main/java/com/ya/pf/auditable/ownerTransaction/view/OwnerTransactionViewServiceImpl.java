package com.ya.pf.auditable.ownerTransaction.view;

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
public class OwnerTransactionViewServiceImpl implements OwnerTransactionViewService {

	private final OwnerTransactionViewRepository ownerTransactionViewRepository;

	@Override
	public Page<OwnerTransactionView> getSupplierTransaction(long supplierId, int pageNo, int pageSize, String sortBy, String order, LocalDate start, LocalDate end) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);
		if (start == null || end == null) {
			return ownerTransactionViewRepository.findAllBySupplierId(supplierId, pageable);
		} else {
			return ownerTransactionViewRepository.findBySupplierIdAndDateBetween(supplierId, Date.valueOf(start),
					Date.valueOf(end.plusDays(1)), pageable);
		}
	}

}
