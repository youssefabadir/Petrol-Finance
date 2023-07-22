package com.ya.pf.auditable.payment.customer_payment;

import com.ya.pf.auditable.payment.PaymentService;
import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentService;
import com.ya.pf.auditable.payment_method.PaymentMethodService;
import com.ya.pf.auditable.transaction.customer_transaction.entity.CustomerTransactionService;
import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestValueException;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerPaymentServiceImpl implements CustomerPaymentService {

    private final CustomerPaymentRepository customerPaymentRepository;

    private final CustomerTransactionService customerTransactionService;

    private final PaymentService paymentService;

    private final PaymentMethodService paymentMethodService;

    private final OwnerPaymentService ownerPaymentService;

    @Override
    public Page<CustomerPaymentEntity> getCustomerPayments(String number, int pageNo, int pageSize, String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

        if (number.isEmpty()) {
            return customerPaymentRepository.findAll(pageable);
        } else {
            return customerPaymentRepository.findByNumberContaining(number, pageable);
        }
    }

    @Override
    @Transactional
    public CustomerPaymentEntity createCustomerPayment(CustomerPaymentEntity customerPayment, long supplierId) throws MissingRequestValueException {

        if (customerPayment.isTransferred() && supplierId == -1) {
            throw new MissingRequestValueException("Supplier Id is missing");
        }
        CustomerPaymentEntity validatedPayment = (CustomerPaymentEntity) paymentService.validatePayment(customerPayment);

        CustomerPaymentEntity payment = customerPaymentRepository.save(validatedPayment);

        if (payment.getCustomer() == null || payment.getCustomer().getId() == null) {
            customerPayment.setCustomer(null);
            customerTransactionService.createCustomerTransaction(payment.getId(), payment.getDate());
        } else {
            customerTransactionService.createCustomerTransaction(payment.getCustomer().getId(),
                                                                 payment.getAmount(),
                                                                 payment.getId(),
                                                                 null,
                                                                 payment.getDate());
        }

        paymentMethodService.updatePaymentMethodBalance(payment.getPaymentMethodId(), payment.getPaymentMethodBalance());

        if (customerPayment.isTransferred()) {
            ownerPaymentService.createOwnerTransferredPayment(customerPayment, supplierId);
        }

        return payment;
    }

}
