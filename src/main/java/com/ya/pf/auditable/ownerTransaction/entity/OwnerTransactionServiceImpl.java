package com.ya.pf.auditable.ownerTransaction.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerTransactionServiceImpl implements OwnerTransactionService {

	private final OwnerTransactionRepository ownerTransactionRepository;

	@Override
	public void createOwnerTransaction(long supplierId, double amount, Long paymentId, Long billId, Date date) {

		double balance = getOwnerSupplierPreviousBalance(supplierId) + amount;

		OwnerTransactionEntity ownerTransactionEntity = new OwnerTransactionEntity();
		ownerTransactionEntity.setOwnerSupplierBalance(balance);
		ownerTransactionEntity.setSupplierId(supplierId);
		ownerTransactionEntity.setOwnerPaymentId(paymentId);
		ownerTransactionEntity.setBillId(billId);
		ownerTransactionEntity.setDate(date);

		ownerTransactionRepository.save(ownerTransactionEntity);
	}

	@Override
	public double getOwnerSupplierPreviousBalance(long supplierId) {

		OwnerTransactionEntity ownerTransactionEntity = ownerTransactionRepository.findFirstBySupplierIdOrderByIdDesc(supplierId);
		if (ownerTransactionEntity == null) {
			return 0;
		} else {
			return ownerTransactionEntity.getOwnerSupplierBalance();
		}
	}

	@Override
	@Transactional
	public void deleteOwnerTransactionByPaymentId(long paymentId, double paymentAmount) {

		ownerTransactionRepository.updatePaymentOwnerSupplierBalance(paymentId, paymentAmount);
		ownerTransactionRepository.deleteByOwnerPaymentId(paymentId);
	}

	@Override
	@Transactional
	public void deleteOwnerTransactionByBillId(long billId, double billAmount) {

		ownerTransactionRepository.updateBillOwnerSupplierBalance(billId, billAmount);
		ownerTransactionRepository.deleteByBillId(billId);
	}

}
