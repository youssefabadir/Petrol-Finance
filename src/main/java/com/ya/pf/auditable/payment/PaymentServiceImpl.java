package com.ya.pf.auditable.payment;

import com.ya.pf.auditable.transaction.customer_transaction.entity.CustomerTransactionService;
import com.ya.pf.auditable.transaction.owner_transaction.entity.OwnerTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final CustomerTransactionService customerTransactionService;

    private final OwnerTransactionService ownerTransactionService;

    @Override
    @Transactional
    public void deletePaymentById(long id) {

        if (paymentRepository.existsById(id)) {
            PaymentEntity payment = paymentRepository.getReferenceById(id);
            if (payment.getPaymentType().equals("CUSTOMER_PAYMENT")) {
                customerTransactionService.deleteCustomerTransactionByPaymentId(payment.getId(), payment.getAmount());
            } else if (payment.getPaymentType().equals("OWNER_PAYMENT")) {
                ownerTransactionService.deleteOwnerTransactionByPaymentId(payment.getId(), payment.getAmount());
            } else {
                throw new EntityNotFoundException("This payment type doesn't exists");
            }
            paymentRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("This payment id " + id + "doesn't exists");
        }
    }

}
