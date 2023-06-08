package com.ya.pf.auditable.payment;

import com.ya.pf.auditable.payment_method.PaymentMethodService;
import com.ya.pf.auditable.transaction.customer_transaction.entity.CustomerTransactionService;
import com.ya.pf.auditable.transaction.owner_transaction.entity.OwnerTransactionService;
import com.ya.pf.util.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestValueException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final CustomerTransactionService customerTransactionService;

    private final OwnerTransactionService ownerTransactionService;

    private final PaymentMethodService paymentMethodService;

    private final Config config;

    @Override
    @Transactional
    public PaymentEntity validatePayment(PaymentEntity payment) throws MissingRequestValueException {

        if (payment.getId() != null) {
            payment.setId(null);
        }

        long paymentMethodId = payment.getPaymentMethodId();
        String paymentType = payment.getPaymentType();

        String paymentNumber = payment.getNumber().trim();
        if (paymentNumber.isEmpty() && paymentMethodId == 1) {
            paymentNumber = UUID.randomUUID().toString();
        }

        if (paymentNumber.isEmpty()) {
            throw new MissingRequestValueException("This payment is missing the payment number");
        }

        boolean exists = paymentRepository.existsByNumberAndPaymentMethodIdAndPaymentTypeEquals(paymentNumber, paymentMethodId, paymentType);
        if (exists) {
            throw new EntityExistsException("This payment number exists for this payment method");
        }

        payment.setNumber(paymentNumber);
        double amount = Math.abs(payment.getAmount());
        payment.setAmount(amount);

        PaymentEntity paymentEntity = paymentRepository.findFirstByOrderByIdDesc();
        double treasuryBalance = paymentEntity == null ? config.getTreasuryBalance()
                                                       : paymentEntity.getTreasury_balance();
        double paymentMethodBalance = paymentMethodService.getPaymentMethodById(paymentMethodId).getBalance();
        if (paymentType.equals("CUSTOMER_PAYMENT")) {
            treasuryBalance = treasuryBalance + amount;
            paymentMethodBalance = paymentMethodBalance + amount;
        } else if (paymentType.equals("OWNER_PAYMENT")) {
            treasuryBalance = treasuryBalance - amount;
            if (!payment.isTransferred()) {
                paymentMethodBalance = paymentMethodBalance - amount;
            }
        } else {
            throw new EntityNotFoundException("This payment method type doesn't exists");
        }
        payment.setTreasury_balance(treasuryBalance);
        payment.setPaymentMethodBalance(paymentMethodBalance);

        return payment;
    }

    @Override
    @Transactional
    public void deletePaymentById(long id) {

        if (paymentRepository.existsById(id)) {
            PaymentEntity payment = paymentRepository.getReferenceById(id);
            long paymentId = payment.getId();
            String paymentNumber = payment.getNumber();
            String paymentType = payment.getPaymentType();
            PaymentEntity transferredPayment = paymentRepository.findByNumberEqualsAndIdNot(paymentNumber, paymentId);
            long transferredPaymentId = transferredPayment.getId();

            if (payment.isTransferred()) {
                customerTransactionService.deleteCustomerTransactionByPaymentId(paymentType.equals("CUSTOMER_PAYMENT") ? paymentId
                                                                                                                       : transferredPaymentId,
                                                                                payment.getAmount());
                ownerTransactionService.deleteOwnerTransactionByPaymentId(paymentType.equals("OWNER_PAYMENT") ? paymentId
                                                                                                              : transferredPaymentId,
                                                                          payment.getAmount());

                paymentRepository.deleteByNumberAndPaymentMethodIdAndTransferredIsTrue(paymentNumber, payment.getPaymentMethodId());
            } else {
                if (payment.getPaymentType().equals("CUSTOMER_PAYMENT")) {
                    customerTransactionService.deleteCustomerTransactionByPaymentId(paymentId, payment.getAmount());
                } else if (payment.getPaymentType().equals("OWNER_PAYMENT")) {
                    ownerTransactionService.deleteOwnerTransactionByPaymentId(paymentId, payment.getAmount());
                } else {
                    throw new EntityNotFoundException("This payment type doesn't exists");
                }

                paymentRepository.deleteById(id);
            }
        } else {
            throw new EntityNotFoundException("This payment id " + id + " doesn't exists");
        }
    }

}
