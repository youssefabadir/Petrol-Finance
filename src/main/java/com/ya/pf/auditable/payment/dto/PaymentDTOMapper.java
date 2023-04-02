package com.ya.pf.auditable.payment.dto;

import com.ya.pf.auditable.payment.PaymentEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PaymentDTOMapper implements Function<PaymentEntity, PaymentDTO> {

	@Override
	public PaymentDTO apply(PaymentEntity paymentEntity) {

		return new PaymentDTO(paymentEntity.getId(),
				paymentEntity.getCustomerEntity(),
				paymentEntity.getReceiptNumber(),
				paymentEntity.getAmount());
	}

}
