package com.ya.pf.auditable.shipment;

import com.ya.pf.auditable.shipment.dto.ShipmentDTO;
import com.ya.pf.auditable.shipment.dto.ShipmentDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/shipment")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    private final ShipmentDTOMapper shipmentDTOMapper;

    @GetMapping
    public ResponseEntity<Page<ShipmentDTO>> getShipments(@RequestParam(defaultValue = "") String billNumber,
                                                          @RequestParam(defaultValue = "0") int pageNo,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                          @RequestParam(defaultValue = "asc") String order) {
        Page<ShipmentEntity> shipmentEntities = shipmentService.getShipments(billNumber,
                                                                             pageNo,
                                                                             pageSize,
                                                                             sortBy,
                                                                             order);
        Page<ShipmentDTO> shipmentDTOS = shipmentEntities.map(shipmentDTOMapper);
        return ResponseEntity.ok(shipmentDTOS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipment(@PathVariable long id) {
        shipmentService.deleteShipment(id);
        return ResponseEntity.noContent().build();
    }

}
