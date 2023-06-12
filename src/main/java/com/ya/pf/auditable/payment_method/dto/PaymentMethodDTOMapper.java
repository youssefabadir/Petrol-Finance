package com.ya.pf.auditable.payment_method.dto;

import com.ya.pf.auditable.payment_method.PaymentMethodEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PaymentMethodDTOMapper implements Function<PaymentMethodEntity, PaymentMethodDTO> {

    @Override
    public PaymentMethodDTO apply(PaymentMethodEntity paymentMethodEntity) {

        return new PaymentMethodDTO(paymentMethodEntity.getId(), paymentMethodEntity.getName(), paymentMethodEntity.getBalance());
    }

}
