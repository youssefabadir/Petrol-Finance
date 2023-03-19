package com.ya.pf.supplier;

import org.springframework.data.domain.Page;

public interface SupplierService {

    Page<SupplierEntity> getSuppliers(String name, int pageNo, int pageSize, String sortBy, String order);

    SupplierEntity createSupplier(SupplierEntity supplierEntity);

    SupplierEntity updateSupplier(SupplierEntity supplierEntity);

    void deleteSupplier(Long id);

}
