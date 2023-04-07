package com.ya.pf.auditable.bill;

import com.ya.pf.auditable.product.ProductService;
import com.ya.pf.auditable.transaction.entity.TransactionService;
import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BillServiceImpl implements BillService {

	private final BillRepository billRepository;

	private final ProductService productService;

	private final TransactionService transactionService;

	@Override
	public Page<BillEntity> getBills(String receiptNumber, int pageNo, int pageSize, String sortBy,
	                                 String order, LocalDate start, LocalDate end) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

		if (!receiptNumber.isEmpty() && start != null && end != null) {
			return billRepository.findByNumberContainingAndDateBetween(receiptNumber, Date.valueOf(start), Date.valueOf(end), pageable);
		} else if (receiptNumber.isEmpty() && start != null && end != null) {
			return billRepository.findByDateBetween(Date.valueOf(start), Date.valueOf(end), pageable);
		} else if (!receiptNumber.isEmpty() && start == null && end == null) {
			return billRepository.findByNumberContaining(receiptNumber, pageable);
		} else {
			return billRepository.findAll(pageable);
		}
	}

	@Override
	@Transactional
	public BillEntity createBill(BillEntity billEntity) {

		if (billEntity.getId() != null) {
			billEntity.setId(null);
		}

		boolean exists = billRepository.existsByNumberAndSupplierEntity_Id(billEntity.getNumber(), billEntity.getSupplierEntity().getId());
		if (exists) {
			throw new EntityExistsException("This bill number exists for this supplier");
		} else {
			double productPrice = productService.getProductPrice(billEntity.getProductEntity().getId());
			double billAmount = productPrice * billEntity.getLiter() * -1;
			billEntity.setAmount(billAmount);

			BillEntity bill = billRepository.save(billEntity);

			transactionService.createTransaction(bill.getCustomerEntity().getId(), bill.getSupplierEntity().getId(),
					billAmount, null, bill.getId(), bill.getDate());

			return bill;
		}
	}

	@Override
	@Transactional
	public void deleteBill(long id) {

		if (billRepository.existsById(id)) {
			BillEntity bill = billRepository.getReferenceById(id);

			billRepository.deleteById(id);
			transactionService.deleteTransactionByBillId(id, bill.getAmount());
		} else {
			throw new EntityNotFoundException("Bill with ID " + id + " not found");
		}
	}

}
