package com.ya.pf.auditable.driver;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Override
    public Page<DriverEntity> getDrivers(String name, int pageNo, int pageSize, String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);
        if (name.trim().isEmpty()) {
            return driverRepository.findAll(pageable);
        } else {
            return driverRepository.findByNameContaining(name, pageable);
        }
    }

    @Override
    public DriverEntity createDriver(DriverEntity driverEntity) {

        if (driverEntity.getId() != null) {
            driverEntity.setId(null);
        }
        return driverRepository.save(driverEntity);
    }

    @Override
    public DriverEntity updateDriver(DriverEntity driverEntity) {

        if (driverRepository.existsById(driverEntity.getId())) {
            return driverRepository.save(driverEntity);
        } else {
            throw new EntityNotFoundException("Driver with ID " + driverEntity.getId() + " not found");
        }
    }

    @Override
    public void deleteDriver(long id) {

        if (driverRepository.existsById(id)) {
            driverRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Driver with ID " + id + " not found");
        }
    }

    @Override
    public List<DriverEntity> searchDriver(String name) {

        return driverRepository.findByNameContaining(name);
    }

}
