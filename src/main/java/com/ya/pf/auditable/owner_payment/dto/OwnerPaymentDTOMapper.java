package com.ya.pf.auditable.owner_payment.dto;

import com.ya.pf.auditable.owner_payment.OwnerPaymentEntity;
import com.ya.pf.auditable.payment_method.dto.PaymentMethodDTOMapper;
import com.ya.pf.auditable.supplier.dto.SupplierDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerPaymentDTOMapper implements Function<OwnerPaymentEntity, OwnerPaymentDTO> {

	private final SupplierDTOMapper supplierDTOMapper;

	private final PaymentMethodDTOMapper paymentMethodDTOMapper;

	@Override
	public OwnerPaymentDTO apply(OwnerPaymentEntity ownerPaymentEntity) {

		return new OwnerPaymentDTO(ownerPaymentEntity.getId(),
				ownerPaymentEntity.getNumber(),
				ownerPaymentEntity.getAmount(),
				supplierDTOMapper.apply(ownerPaymentEntity.getSupplierEntity()),
				paymentMethodDTOMapper.apply(ownerPaymentEntity.getPaymentMethodEntity()),
				ownerPaymentEntity.isTransferred(),
				ownerPaymentEntity.getDate());
	}

}
