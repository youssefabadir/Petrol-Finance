package com.ya.pf.auditable.payment.owner_payment;

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
public class OwnerPaymentServiceImpl implements OwnerPaymentService {

    private final OwnerPaymentRepository ownerPaymentRepository;

    @Override
    public Page<OwnerPaymentEntity> getOwnerPayments(String number, int pageNo, int pageSize, String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

        if (number.isEmpty()) {
            return ownerPaymentRepository.findAll(pageable);
        } else {
            return ownerPaymentRepository.findByNumberContaining(number, pageable);
        }
    }

    @Override
    @Transactional
    public OwnerPaymentEntity createOwnerPayment(OwnerPaymentEntity ownerPayment) throws MissingRequestValueException {

        if (ownerPayment.getId() != null) {
            ownerPayment.setId(null);
        }

        long paymentMethodId = ownerPayment.getPaymentMethodId();
        String paymentNumber = ownerPayment.getNumber().trim();
        if (paymentMethodId != 1) {
            if (paymentNumber.isEmpty()) {
                throw new MissingRequestValueException("This payment is missing the payment number");
            }

            boolean exists = ownerPaymentRepository.existsByNumberAndPaymentMethodId(paymentNumber, paymentMethodId);
            if (exists) {
                throw new EntityExistsException("This payment number exists for this payment method");
            }
        }
        ownerPayment.setAmount(Math.abs(ownerPayment.getAmount()));

        // TODO: Save owner transaction

        return ownerPaymentRepository.save(ownerPayment);
    }


}
