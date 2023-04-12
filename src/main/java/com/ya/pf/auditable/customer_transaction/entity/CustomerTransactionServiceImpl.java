package com.ya.pf.auditable.customer_transaction.entity;

import com.ya.pf.auditable.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerTransactionServiceImpl implements CustomerTransactionService {

	private final CustomerTransactionRepository customerTransactionRepository;

	private final CustomerService customerService;

	@Override
	public void createCustomerTransaction(long customerId, double amount, Long paymentId, Long billId, Date date) {

		double balance = getCustomerPreviousBalance(customerId) + amount;

		CustomerTransactionEntity customerTransactionEntity = new CustomerTransactionEntity();
		customerTransactionEntity.setCustomerId(customerId);
		customerTransactionEntity.setCustomerBalance(balance);
		customerTransactionEntity.setCustomerPaymentId(paymentId);
		customerTransactionEntity.setBillId(billId);
		customerTransactionEntity.setDate(date);

		customerTransactionRepository.save(customerTransactionEntity);
	}

	@Override
	public double getCustomerPreviousBalance(long customerId) {

		CustomerTransactionEntity customerTransactionEntity = customerTransactionRepository.findFirstByCustomerIdOrderByIdDesc(customerId);
		if (customerTransactionEntity == null) {
			return customerService.getCustomerById(customerId).getBalance();
		} else {
			return customerTransactionEntity.getCustomerBalance();
		}
	}

	@Override
	@Transactional
	public void deleteCustomerTransactionByPaymentId(long paymentId, double paymentAmount) {

		customerTransactionRepository.updatePaymentCustomerBalance(paymentId, paymentAmount * -1);
		customerTransactionRepository.deleteByCustomerPaymentId(paymentId);
	}

	@Override
	@Transactional
	public void deleteCustomerTransactionByBillId(long billId, double billAmount) {

		customerTransactionRepository.updateBillCustomerBalance(billId, Math.abs(billAmount));
		customerTransactionRepository.deleteByBillId(billId);
	}

}
