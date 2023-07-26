package com.ya.pf.auditable.supplier;

import org.springframework.data.domain.Page;

import java.util.List;

public interface SupplierService {

    Page<SupplierEntity> getSuppliers(String name, int pageNo, int pageSize, String sortBy, String order);

    SupplierEntity createSupplier(SupplierEntity supplierEntity);

    SupplierEntity updateSupplier(SupplierEntity supplierEntity);

    void deleteSupplier(long id);


    List<SupplierEntity> searchSupplier(String name);

    SupplierEntity getSupplierById(long id);

    void updateSupplierBalance(long supplierId, float supplierBalance);

    void enableDeletedSupplierFilter();

}
