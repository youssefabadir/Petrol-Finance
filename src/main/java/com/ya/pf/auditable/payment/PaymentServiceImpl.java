package com.ya.pf.auditable.payment;

import com.ya.pf.auditable.payment.customer_payment.CustomerPaymentEntity;
import com.ya.pf.auditable.payment.customer_payment.CustomerPaymentRepository;
import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentEntity;
import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentRepository;
import com.ya.pf.auditable.payment_method.PaymentMethodEntity;
import com.ya.pf.auditable.payment_method.PaymentMethodService;
import com.ya.pf.auditable.transaction.customer_transaction.entity.CustomerTransactionService;
import com.ya.pf.auditable.transaction.owner_transaction.entity.OwnerTransactionService;
import com.ya.pf.util.Config;
import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestValueException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final CustomerTransactionService customerTransactionService;

    private final OwnerTransactionService ownerTransactionService;

    private final PaymentMethodService paymentMethodService;

    private final CustomerPaymentRepository customerPaymentRepository;

    private final OwnerPaymentRepository ownerPaymentRepository;

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
        java.util.Date date = payment.getDate();
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
        float amount = Math.abs(payment.getAmount());
        payment.setAmount(amount);

        PaymentEntity paymentEntity = paymentRepository.findFirstByPaymentMethodIdAndDateLessThanEqualOrderByIdDesc(paymentMethodId, date);
        float treasuryBalance = paymentEntity == null ? config.getTreasuryBalance()
                                                      : paymentEntity.getTreasury_balance();
        PaymentMethodEntity paymentMethod = paymentMethodService.getPaymentMethodById(paymentMethodId);
        float paymentMethodBalance = paymentEntity == null ? paymentMethod.getStartBalance() : paymentEntity.getPaymentMethodBalance();
        if (paymentType.equals("CUSTOMER_PAYMENT") && !payment.isTransferred()) {
            treasuryBalance = treasuryBalance + amount;
            paymentMethodBalance = paymentMethodBalance + amount;
            paymentRepository.updatePaymentMethodBalance(paymentMethodId, amount, date);
        } else if (paymentType.equals("OWNER_PAYMENT") && !payment.isTransferred()) {
            treasuryBalance = treasuryBalance - amount;
            paymentMethodBalance = paymentMethodBalance - amount;
            paymentRepository.updatePaymentMethodBalance(paymentMethodId, amount * -1, date);
        }
        payment.setTreasury_balance(treasuryBalance);
        payment.setPaymentMethodBalance(paymentMethodBalance);
        payment.setPaymentMethodName(paymentMethod.getName());

        return payment;
    }

    @Override
    public Page<PaymentEntity> getPayments(long paymentMethodId, int pageNo, int pageSize,
                                           String sortBy, String order, LocalDate start, LocalDate end) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);
        if (start == null || end == null) {
            if (paymentMethodId == -1) {
                return paymentRepository.findAll(pageable);
            } else {
                return paymentRepository.findByPaymentMethodId(paymentMethodId, pageable);
            }
        } else {
            if (paymentMethodId == -1) {
                return paymentRepository.findAllByDateBetween(Date.valueOf(start), Date.valueOf(end.plusDays(1)), pageable);
            } else {
                return paymentRepository.findByPaymentMethodIdAndDateBetween(paymentMethodId, Date.valueOf(start),
                                                                             Date.valueOf(end.plusDays(1)), pageable);
            }
        }
    }

    @Override
    @Transactional
    public void deletePaymentById(long id) {

        if (paymentRepository.existsById(id)) {
            PaymentEntity payment = paymentRepository.getReferenceById(id);
            long paymentId = payment.getId();
            String paymentNumber = payment.getNumber();
            String paymentType = payment.getPaymentType();
            boolean isTransferred = payment.isTransferred();

            if (isTransferred) {
                PaymentEntity transferredPayment = paymentRepository.findByNumberEqualsAndIdNot(paymentNumber, paymentId);
                if (paymentType.equals("CUSTOMER_PAYMENT")) {
                    CustomerPaymentEntity customerPayment = customerPaymentRepository.getReferenceById(id);
                    OwnerPaymentEntity ownerPayment = ownerPaymentRepository.getReferenceById(transferredPayment.getId());
                    customerTransactionService.deleteCustomerTransactionByPaymentId(customerPayment.getCustomer().getId(), paymentId, payment.getAmount(), payment.getDate());
                    ownerTransactionService.deleteOwnerTransactionByPaymentId(ownerPayment.getSupplier().getId(), transferredPayment.getId(), payment.getAmount(), payment.getDate());
                } else {
                    CustomerPaymentEntity customerPayment = customerPaymentRepository.getReferenceById(transferredPayment.getId());
                    OwnerPaymentEntity ownerPayment = ownerPaymentRepository.getReferenceById(id);
                    customerTransactionService.deleteCustomerTransactionByPaymentId(customerPayment.getCustomer().getId(), transferredPayment.getId(), payment.getAmount(), payment.getDate());
                    ownerTransactionService.deleteOwnerTransactionByPaymentId(ownerPayment.getSupplier().getId(), paymentId, payment.getAmount(), payment.getDate());
                }
                paymentRepository.deleteByNumberAndPaymentMethodIdAndTransferredIsTrue(paymentNumber, payment.getPaymentMethodId());
            } else {
                if (paymentType.equals("CUSTOMER_PAYMENT")) {
                    CustomerPaymentEntity customerPayment = customerPaymentRepository.getReferenceById(id);
                    customerTransactionService.deleteCustomerTransactionByPaymentId(customerPayment.getCustomer().getId(), paymentId, payment.getAmount(), payment.getDate());
                    paymentMethodService.updatePaymentMethodBalance(payment.getPaymentMethodId(), payment.getPaymentMethodBalance() - payment.getAmount());
                    paymentRepository.updatePaymentMethodBalanceById(payment.getPaymentMethodId(), id, payment.getAmount() * -1, payment.getDate());
                } else {
                    OwnerPaymentEntity ownerPayment = ownerPaymentRepository.getReferenceById(id);
                    ownerTransactionService.deleteOwnerTransactionByPaymentId(ownerPayment.getSupplier().getId(), paymentId, payment.getAmount(), payment.getDate());
                    paymentMethodService.updatePaymentMethodBalance(payment.getPaymentMethodId(), payment.getPaymentMethodBalance() + payment.getAmount());
                    paymentRepository.updatePaymentMethodBalanceById(payment.getPaymentMethodId(), id, payment.getAmount(), payment.getDate());
                }
                paymentRepository.deleteById(id);
            }
        } else {
            throw new EntityNotFoundException("This payment id " + id + " doesn't exists");
        }
    }

}
