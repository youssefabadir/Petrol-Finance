package com.ya.pf.auditable.transaction.customer_transaction.entity;

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
    public void createCustomerTransaction(Long paymentId, Date date) {

        CustomerTransactionEntity customerTransaction = new CustomerTransactionEntity();
        customerTransaction.setPaymentId(paymentId);
        customerTransaction.setDate(date);
        customerTransactionRepository.save(customerTransaction);
    }

    @Override
    @Transactional
    public void createCustomerTransaction(long customerId, float amount, Long paymentId, Long billId, Date date) {

        float newBalance = customerService.getCustomerById(customerId).getBalance() + amount;

        CustomerTransactionEntity customerTransaction = new CustomerTransactionEntity();
        customerTransaction.setCustomerId(customerId);
        customerTransaction.setCustomerBalance(newBalance);
        customerTransaction.setPaymentId(paymentId);
        customerTransaction.setBillId(billId);
        customerTransaction.setDate(date);

        customerTransactionRepository.save(customerTransaction);

        customerService.updateCustomerBalance(customerId, newBalance);
    }

    @Override
    @Transactional
    public void deleteCustomerTransactionByBillId(long billId, float billAmount) {

        customerTransactionRepository.updateCustomerBalanceByBillId(billId, billAmount);
        CustomerTransactionEntity customerTransaction = customerTransactionRepository.findByBillId(billId);
        customerService.updateCustomerBalance(customerTransaction.getCustomerId(), customerTransaction.getCustomerBalance() + billAmount);
        customerTransactionRepository.deleteByBillId(billId);
    }

    @Override
    @Transactional
    public void deleteCustomerTransactionByPaymentId(long paymentId, float paymentAmount) {

        customerTransactionRepository.updateCustomerBalanceByPaymentId(paymentId, Math.abs(paymentAmount) * -1);
        CustomerTransactionEntity customerTransaction = customerTransactionRepository.findByPaymentId(paymentId);
        if (customerTransaction.getCustomerId() != null) {
            customerService.updateCustomerBalance(customerTransaction.getCustomerId(), customerTransaction.getCustomerBalance() - paymentAmount);
        }
        customerTransactionRepository.deleteByPaymentId(paymentId);
    }

}
