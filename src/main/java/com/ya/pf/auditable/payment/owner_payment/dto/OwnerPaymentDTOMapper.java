package com.ya.pf.auditable.payment.owner_payment.dto;

import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OwnerPaymentDTOMapper implements Function<OwnerPaymentEntity, OwnerPaymentDTO> {

    @Override
    public OwnerPaymentDTO apply(OwnerPaymentEntity ownerPaymentEntity) {

        return new OwnerPaymentDTO(ownerPaymentEntity.getId(),
                                   ownerPaymentEntity.getNumber(),
                                   ownerPaymentEntity.getAmount(),
                                   ownerPaymentEntity.getPaymentMethodId(),
                                   ownerPaymentEntity.getSupplierId(),
                                   ownerPaymentEntity.isTransferred(),
                                   ownerPaymentEntity.getDate());
    }

}
