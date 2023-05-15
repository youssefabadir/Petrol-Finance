package com.ya.pf.auditable.truck;

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
public class TruckServiceImpl implements TruckService {

    private final TruckRepository truckRepository;

    @Override
    public Page<TruckEntity> getTrucks(String number, int pageNo, int pageSize, String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);
        if (number.trim().isEmpty()) {
            return truckRepository.findAll(pageable);
        } else {
            return truckRepository.findByNumberContaining(number, pageable);
        }
    }

    @Override
    public TruckEntity createTruck(TruckEntity truckEntity) {

        if (truckEntity.getId() != null) {
            truckEntity.setId(null);
        }
        return truckRepository.save(truckEntity);
    }

    @Override
    public TruckEntity updateTruck(TruckEntity truckEntity) {

        if (truckRepository.existsById(truckEntity.getId())) {
            return truckRepository.save(truckEntity);
        } else {
            throw new EntityNotFoundException("Truck with ID " + truckEntity.getId() + " not found");
        }
    }

    @Override
    public void deleteTruck(long id) {

        if (truckRepository.existsById(id)) {
            truckRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Truck with ID " + id + " not found");
        }
    }

    @Override
    public List<TruckEntity> searchTruck(String number) {

        return truckRepository.findByNumberContaining(number);
    }

}
