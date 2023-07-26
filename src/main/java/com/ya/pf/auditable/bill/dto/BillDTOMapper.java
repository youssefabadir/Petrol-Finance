package com.ya.pf.auditable.bill.dto;

import com.ya.pf.auditable.bill.BillEntity;
import com.ya.pf.auditable.customer.dto.CustomerDTOMapper;
import com.ya.pf.auditable.product.dto.ProductDTOMapper;
import com.ya.pf.auditable.supplier.dto.SupplierDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BillDTOMapper implements Function<BillEntity, BillDTO> {

    private final SupplierDTOMapper supplierDTOMapper;

    private final CustomerDTOMapper customerDTOMapper;

    private final ProductDTOMapper productDTOMapper;

    @Override
    public BillDTO apply(BillEntity billEntity) {

        return new BillDTO(
                billEntity.getId(),
                supplierDTOMapper.apply(billEntity.getSupplierEntity()),
                customerDTOMapper.apply(billEntity.getCustomerEntity()),
                productDTOMapper.apply(billEntity.getProductEntity()),
                billEntity.getNumber(),
                billEntity.getQuantity(),
                billEntity.getSupplierAmount(),
                billEntity.getCustomerAmount(),
                billEntity.getDate()
        );
    }

}
