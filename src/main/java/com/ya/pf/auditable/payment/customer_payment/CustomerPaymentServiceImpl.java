package com.ya.pf.auditable.payment.customer_payment;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestValueException;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerPaymentServiceImpl implements CustomerPaymentService {

    private final CustomerPaymentRepository customerPaymentRepository;

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
    public CustomerPaymentEntity createCustomerPayment(CustomerPaymentEntity customerPayment) throws MissingRequestValueException {

        if (customerPayment.getId() != null) {
            customerPayment.setId(null);
        }

        long paymentMethodId = customerPayment.getPaymentMethodId();
        String paymentNumber = customerPayment.getNumber().trim();
        if (paymentMethodId != 1) {
            if (paymentNumber.isEmpty()) {
                throw new MissingRequestValueException("This payment is missing the payment number");
            }

            boolean exists = customerPaymentRepository.existsByNumberAndPaymentMethodId(paymentNumber, paymentMethodId);
            if (exists) {
                throw new EntityExistsException("This payment number exists for this payment method");
            }
        }
        customerPayment.setAmount(Math.abs(customerPayment.getAmount()));

        // TODO: Save customer transaction

        return customerPaymentRepository.save(customerPayment);
    }

}
