package com.ya.pf.auditable.payment.owner_payment;

import com.ya.pf.auditable.payment.PaymentEntity;
import com.ya.pf.auditable.payment.PaymentService;
import com.ya.pf.auditable.payment_method.PaymentMethodService;
import com.ya.pf.auditable.supplier.SupplierEntity;
import com.ya.pf.auditable.supplier.SupplierService;
import com.ya.pf.auditable.transaction.owner_transaction.entity.OwnerTransactionService;
import com.ya.pf.util.Helper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestValueException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerPaymentServiceImpl implements OwnerPaymentService {

    private final OwnerPaymentRepository ownerPaymentRepository;

    private final OwnerTransactionService ownerTransactionService;

    private final PaymentService paymentService;

    private final PaymentMethodService paymentMethodService;

    private final SupplierService supplierService;

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

        OwnerPaymentEntity validatedPayment = (OwnerPaymentEntity) paymentService.validatePayment(ownerPayment);

        OwnerPaymentEntity payment = ownerPaymentRepository.save(validatedPayment);

        if (payment.getSupplier() == null || payment.getSupplier().getId() == null) {
            payment.setSupplier(null);
            ownerTransactionService.createOwnerTransaction(payment.getId(), payment.getDate());
        } else {
            ownerTransactionService.createOwnerTransaction(payment.getSupplier().getId(),
                                                           payment.getAmount(),
                                                           payment.getId(),
                                                           null,
                                                           payment.getDate());
        }

        paymentMethodService.updatePaymentMethodBalance(payment.getPaymentMethodId(),
                                                        payment.getPaymentMethodBalance());

        return payment;
    }

    @Override
    @Transactional
    public OwnerPaymentEntity updateOwnerPayment(OwnerPaymentEntity ownerPayment) throws MissingRequestValueException {

        long paymentId = ownerPayment.getId();
        paymentService.deletePaymentById(paymentId);
        return createOwnerPayment(ownerPayment);
    }

    @Override
    @Transactional
    public void createOwnerTransferredPayment(PaymentEntity payment, long supplierId) throws MissingRequestValueException {

        SupplierEntity supplier = supplierService.getSupplierById(supplierId);
        OwnerPaymentEntity ownerPayment = new OwnerPaymentEntity();
        ownerPayment.setPaymentType("OWNER_PAYMENT");
        ownerPayment.setNumber(payment.getNumber());
        ownerPayment.setAmount(payment.getAmount());
        ownerPayment.setSupplier(supplier);
        ownerPayment.setTransferred(payment.isTransferred());
        ownerPayment.setPaymentMethodId(payment.getPaymentMethodId());
        ownerPayment.setPaymentMethodName(payment.getPaymentMethodName());
        ownerPayment.setDate(payment.getDate());

        createOwnerPayment(ownerPayment);
    }


}
