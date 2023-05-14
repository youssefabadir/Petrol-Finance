package com.ya.pf.auditable.driver.dto;

import com.ya.pf.auditable.driver.DriverEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DriverDTOMapper implements Function<DriverEntity, DriverDTO> {

    @Override
    public DriverDTO apply(DriverEntity driverEntity) {

        return new DriverDTO(driverEntity.getId(), driverEntity.getName());
    }

}
