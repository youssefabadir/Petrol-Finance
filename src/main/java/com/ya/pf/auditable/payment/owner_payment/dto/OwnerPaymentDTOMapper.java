package com.ya.pf.auditable.payment.owner_payment.dto;

import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentEntity;
import com.ya.pf.auditable.supplier.dto.SupplierDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerPaymentDTOMapper implements Function<OwnerPaymentEntity, OwnerPaymentDTO> {

    private final SupplierDTOMapper supplierDTOMapper;

    @Override
    public OwnerPaymentDTO apply(OwnerPaymentEntity ownerPaymentEntity) {

        return new OwnerPaymentDTO(ownerPaymentEntity.getId(),
                                   ownerPaymentEntity.getNumber(),
                                   ownerPaymentEntity.getAmount(),
                                   ownerPaymentEntity.getPaymentMethodName(),
                                   supplierDTOMapper.apply(ownerPaymentEntity.getSupplier()),
                                   ownerPaymentEntity.isTransferred(),
                                   ownerPaymentEntity.getDate());
    }

}
