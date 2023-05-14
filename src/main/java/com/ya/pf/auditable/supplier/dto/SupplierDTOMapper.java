package com.ya.pf.auditable.supplier.dto;

import com.ya.pf.auditable.supplier.SupplierEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SupplierDTOMapper implements Function<SupplierEntity, SupplierDTO> {

    @Override
    public SupplierDTO apply(SupplierEntity supplierEntity) {

        return new SupplierDTO(supplierEntity.getId(), supplierEntity.getName(), supplierEntity.getBalance());
    }

}
