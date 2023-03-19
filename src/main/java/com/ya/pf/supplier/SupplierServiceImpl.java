package com.ya.pf.supplier;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public Page<SupplierEntity> getSuppliers(String name, int pageNo, int pageSize,
                                             String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

        if (name.isEmpty()) {
            return supplierRepository.findAll(pageable);
        } else {
            return supplierRepository.findByNameContaining(name, pageable);
        }
    }

    @Override
    public SupplierEntity createSupplier(SupplierEntity supplierEntity) {

        return supplierRepository.save(supplierEntity);
    }

    @Override
    public SupplierEntity updateSupplier(SupplierEntity supplierEntity) {

        return supplierRepository.save(supplierEntity);
    }

    @Override
    public void deleteSupplier(Long id) {

        supplierRepository.deleteById(id);
    }

}
