package com.ya.pf.auditable.payment.dto;

import com.ya.pf.auditable.customer.dto.CustomerDTOMapper;
import com.ya.pf.auditable.payment.PaymentEntity;
import com.ya.pf.auditable.payment.customer_payment.CustomerPaymentEntity;
import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentEntity;
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

    @Override
    public PaymentDTO apply(PaymentEntity payment) {

        if (payment instanceof CustomerPaymentEntity entity) {
            return new PaymentDTO(payment.getNumber(),
                                  payment.getAmount(),
                                  payment.getPaymentMethodId(),
                                  payment.getPaymentMethodName(),
                                  payment.getPaymentMethodBalance(),
                                  payment.getTreasury_balance(),
                                  customerDTOMapper.apply(entity.getCustomer()),
                                  null,
                                  payment.getNote(),
                                  payment.getDate());
        } else {
            return new PaymentDTO(payment.getNumber(),
                                  payment.getAmount(),
                                  payment.getPaymentMethodId(),
                                  payment.getPaymentMethodName(),
                                  payment.getPaymentMethodBalance(),
                                  payment.getTreasury_balance(),
                                  null,
                                  supplierDTOMapper.apply(((OwnerPaymentEntity) payment).getSupplier()),
                                  payment.getNote(),
                                  payment.getDate());
        }
    }

}
