package com.ya.pf.auditable.transaction.entity;

import com.ya.pf.auditable.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;

	private final CustomerService customerService;

	@Override
	public void createTransaction(long customerId, long supplierId, double amount, Long paymentId, Long billId, Date date) {

		double balance = getPreviousBalance(customerId) + amount;

		TransactionEntity transactionEntity = new TransactionEntity();
		transactionEntity.setCustomerId(customerId);
		transactionEntity.setSupplierId(supplierId);
		transactionEntity.setCustomerBalance(balance);
		transactionEntity.setPaymentId(paymentId);
		transactionEntity.setBillId(billId);
		transactionEntity.setDate(date);

		transactionRepository.save(transactionEntity);
	}

	@Override
	public double getPreviousBalance(long customerId) {

		TransactionEntity transactionEntity = transactionRepository.findFirstByCustomerIdOrderByIdDesc(customerId);
		if (transactionEntity == null) {
			return customerService.getCustomerById(customerId).getBalance();
		} else {
			return transactionEntity.getCustomerBalance();
		}
	}

	@Override
	@Transactional
	public void deleteTransactionByPaymentId(long paymentId, double paymentAmount) {

		transactionRepository.updatePaymentCustomerBalance(paymentId, paymentAmount * -1);
		transactionRepository.deleteAllByPaymentId(paymentId);
	}

	@Override
	@Transactional
	public void deleteTransactionByBillId(long billId, double billAmount) {

		transactionRepository.updateBillCustomerBalance(billId, Math.abs(billAmount));
		transactionRepository.deleteAllByBillId(billId);
	}

}
