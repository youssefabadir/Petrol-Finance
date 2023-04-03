package com.ya.pf.auditable.payment.dto;

import com.ya.pf.auditable.customer.dto.CustomerDTOMapper;
import com.ya.pf.auditable.payment.PaymentEntity;
import com.ya.pf.auditable.wayofpayment.dto.WayOfPaymentDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentDTOMapper implements Function<PaymentEntity, PaymentDTO> {

	private final CustomerDTOMapper customerDTOMapper;

	private final WayOfPaymentDTOMapper wayOfPaymentDTOMapper;

	@Override
	public PaymentDTO apply(PaymentEntity paymentEntity) {

		return new PaymentDTO(paymentEntity.getId(),
				customerDTOMapper.apply(paymentEntity.getCustomerEntity()),
				wayOfPaymentDTOMapper.apply(paymentEntity.getWayOfPaymentEntity()),
				paymentEntity.getReceiptNumber(),
				paymentEntity.getAmount(),
				paymentEntity.getPaymentDate());
	}

}
