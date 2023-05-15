package com.ya.pf.auditable.truck.dto;

import com.ya.pf.auditable.truck.TruckEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TruckDTOMapper implements Function<TruckEntity, TruckDTO> {

    @Override
    public TruckDTO apply(TruckEntity truckEntity) {

        return new TruckDTO(truckEntity.getId(), truckEntity.getNumber());
    }

}
