package com.ya.pf.auditable.driver;

import org.springframework.data.domain.Page;

import java.util.List;

public interface DriverService {

    Page<DriverEntity> getDrivers(String name, int pageNo, int pageSize, String sortBy, String order);

    DriverEntity createDriver(DriverEntity driverEntity);

    DriverEntity updateDriver(DriverEntity driverEntity);

    void deleteDriver(long id);

    List<DriverEntity> searchDriver(String name);

}
