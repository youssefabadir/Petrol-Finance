package com.ya.pf.auditable.payment_method;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public Page<PaymentMethodEntity> getWayOfPayments(String name, int pageNo, int pageSize, String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

        if (name.trim().isEmpty()) {
            return paymentMethodRepository.findAll(pageable);
        } else {
            return paymentMethodRepository.findByNameContaining(name, pageable);
        }
    }

    @Override
    public PaymentMethodEntity createWayOfPayment(PaymentMethodEntity paymentMethodEntity) {

        if (paymentMethodRepository.existsByName(paymentMethodEntity.getName())) {
            throw new EntityExistsException("Way of payment with this name already exists");
        }

        if (paymentMethodEntity.getId() != null) {
            paymentMethodEntity.setId(null);
        }

        return paymentMethodRepository.save(paymentMethodEntity);
    }

    @Override
    public PaymentMethodEntity updateWayOfPayment(PaymentMethodEntity paymentMethodEntity) {

        long id = paymentMethodEntity.getId();
        if (paymentMethodRepository.existsById(id)) {
            boolean uniqueWayOfPayment = paymentMethodRepository.checkUniquePayment(id, paymentMethodEntity.getName());

            if (uniqueWayOfPayment) {
                return paymentMethodRepository.save(paymentMethodEntity);
            } else {
                throw new EntityExistsException("Way of payment with this name already exists");
            }
        } else {
            throw new EntityNotFoundException("Way of payment with Id " + id + " not found");
        }
    }

    @Override
    public void deleteWayOfPayment(long id) {

        if (paymentMethodRepository.existsById(id)) {
            paymentMethodRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Way of payment with Id " + id + " not found");
        }
    }

    @Override
    public List<PaymentMethodEntity> searchPaymentMethod(String name) {

        return paymentMethodRepository.findByNameContaining(name);
    }

}
