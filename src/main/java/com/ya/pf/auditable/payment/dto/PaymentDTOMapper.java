package com.ya.pf.auditable.payment.dto;

import com.ya.pf.auditable.customer.dto.CustomerDTOMapper;
import com.ya.pf.auditable.payment.PaymentEntity;
import com.ya.pf.auditable.paymentMethod.dto.PaymentMethodDTOMapper;
import com.ya.pf.auditable.supplier.dto.SupplierDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentDTOMapper implements Function<PaymentEntity, PaymentDTO> {

	private final CustomerDTOMapper customerDTOMapper;

	private final SupplierDTOMapper supplierDTOMapper;

	private final PaymentMethodDTOMapper paymentMethodDTOMapper;

	@Override
	public PaymentDTO apply(PaymentEntity paymentEntity) {

		return new PaymentDTO(paymentEntity.getId(),
				customerDTOMapper.apply(paymentEntity.getCustomerEntity()),
				supplierDTOMapper.apply(paymentEntity.getSupplierEntity()),
				paymentMethodDTOMapper.apply(paymentEntity.getPaymentMethodEntity()),
				paymentEntity.getNumber(),
				paymentEntity.getAmount(),
				paymentEntity.getDate());
	}

}
