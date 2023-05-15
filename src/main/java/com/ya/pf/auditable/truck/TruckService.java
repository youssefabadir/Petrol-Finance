package com.ya.pf.auditable.truck;

import org.springframework.data.domain.Page;

import java.util.List;

public interface TruckService {

    Page<TruckEntity> getTrucks(String number, int pageNo, int pageSize, String sortBy, String order);

    TruckEntity createTruck(TruckEntity truckEntity);

    TruckEntity updateTruck(TruckEntity truckEntity);

    void deleteTruck(long id);

    List<TruckEntity> searchTruck(String number);

}
