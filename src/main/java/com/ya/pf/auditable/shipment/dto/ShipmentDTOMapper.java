package com.ya.pf.auditable.shipment.dto;

import com.ya.pf.auditable.expense.dto.ExpenseDTOMapper;
import com.ya.pf.auditable.shipment.ShipmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShipmentDTOMapper implements Function<ShipmentEntity, ShipmentDTO> {

    private final ExpenseDTOMapper expenseDTOMapper;

    @Override
    public ShipmentDTO apply(ShipmentEntity shipmentEntity) {

        return new ShipmentDTO(shipmentEntity.getId(),
                               shipmentEntity.getBillEntity().getNumber(),
                               shipmentEntity.getTruckEntity().getNumber(),
                               shipmentEntity.getTruckBalance(),
                               shipmentEntity.getRevenue(),
                               shipmentEntity.getNote(),
                               shipmentEntity.getExpenseEntities().stream().map(expenseDTOMapper).collect(Collectors.toSet()));
    }

}
