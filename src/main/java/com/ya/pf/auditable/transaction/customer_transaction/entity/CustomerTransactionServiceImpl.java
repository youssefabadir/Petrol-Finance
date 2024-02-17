package com.ya.pf.auditable.transaction.customer_transaction.entity;

import com.ya.pf.auditable.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
@Transactional
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
    public void createCustomerTransaction(long customerId, float amount, Long paymentId, Long billId, Date date) {

        float newBalance;
        try {
            newBalance = customerTransactionRepository.findFirstByCustomerIdAndDateLessThanEqualOrderByIdDesc(customerId, date)
                    .getCustomerBalance() + amount;
        } catch (Exception e) {
            log.warn("Couldn't find previous balance for customer " + customerId + " for date " + date);
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            newBalance = customerService.getCustomerById(customerId).getBalance() + amount;
        }
        CustomerTransactionEntity customerTransaction = new CustomerTransactionEntity();
        customerTransaction.setCustomerId(customerId);
        customerTransaction.setCustomerBalance(newBalance);
        customerTransaction.setPaymentId(paymentId);
        customerTransaction.setBillId(billId);
        customerTransaction.setDate(date);

        customerTransactionRepository.save(customerTransaction);

        customerTransactionRepository.updateCustomerBalanceByCustomerIdAfterDate(amount, customerId, date);

        newBalance = customerTransactionRepository.findFirstByOrderByDateDescIdDesc().getCustomerBalance();

        customerService.updateCustomerBalance(customerId, newBalance);
    }

    @Override
    public void deleteCustomerTransactionByBillId(long customerId, long billId, float billAmount, Date date) {

        customerTransactionRepository.updateCustomerBalanceByBillId(customerId, billId, billAmount, date);
        CustomerTransactionEntity customerTransaction = customerTransactionRepository.findByBillId(billId);
        customerService.updateCustomerBalance(customerTransaction.getCustomerId(), customerTransaction.getCustomerBalance() + billAmount);
        customerTransactionRepository.deleteByBillId(billId);
    }

    @Override
    public void deleteCustomerTransactionByPaymentId(long customerId, long paymentId, float paymentAmount, Date date) {

        customerTransactionRepository.updateCustomerBalanceByPaymentId(customerId, paymentId, Math.abs(paymentAmount) * -1, date);
        CustomerTransactionEntity customerTransaction = customerTransactionRepository.findByPaymentId(paymentId);
        if (customerTransaction.getCustomerId() != null) {
            customerService.updateCustomerBalance(customerTransaction.getCustomerId(), customerTransaction.getCustomerBalance() - paymentAmount);
        }
        customerTransactionRepository.deleteByPaymentId(paymentId);
    }

}
