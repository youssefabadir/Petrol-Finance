package com.ya.pf.auditable.paymentMethod.dto;

import com.ya.pf.auditable.paymentMethod.PaymentMethodEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PaymentMethodDTOMapper implements Function<PaymentMethodEntity, PaymentMethodDTO> {

	@Override
	public PaymentMethodDTO apply(PaymentMethodEntity paymentMethodEntity) {

		return new PaymentMethodDTO(paymentMethodEntity.getId(), paymentMethodEntity.getName());
	}

}
