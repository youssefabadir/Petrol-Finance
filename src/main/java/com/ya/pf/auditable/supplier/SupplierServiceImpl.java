package com.ya.pf.auditable.supplier;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public Page<SupplierEntity> getSuppliers(String name, int pageNo, int pageSize,
                                             String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

        if (name.trim().isEmpty()) {
            return supplierRepository.findAll(pageable);
        } else {
            return supplierRepository.findByNameContaining(name, pageable);
        }
    }

    @Override
    public SupplierEntity createSupplier(SupplierEntity supplierEntity) {

        if (supplierEntity.getId() != null) {
            supplierEntity.setId(null);
        }

        return supplierRepository.save(supplierEntity);
    }

    @Override
    public SupplierEntity updateSupplier(SupplierEntity supplierEntity) {

        if (supplierRepository.existsById(supplierEntity.getId())) {
            return supplierRepository.save(supplierEntity);
        } else {
            throw new EntityNotFoundException("Supplier with ID " + supplierEntity.getId() + " not found");
        }
    }

    @Override
    public void deleteSupplier(long id) {

        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Supplier with ID " + id + " not found");
        }
    }

    @Override
    public SupplierEntity getSupplierById(long id) {

        return supplierRepository.getReferenceById(id);
    }

    @Override
    @Transactional
    public void updateSupplierBalance(long supplierId, double supplierBalance) {

        supplierRepository.updateSupplierBalance(supplierId, supplierBalance);
    }

    @Override
    public List<SupplierEntity> searchSupplier(String name) {

        return supplierRepository.findByNameContaining(name);
    }

}
