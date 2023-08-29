package com.ya.pf.auditable.shipment;

import com.ya.pf.auditable.shipment.dto.ShipmentDTO;
import com.ya.pf.auditable.shipment.dto.ShipmentDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/shipment")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShipmentController {

    private final ShipmentService shipmentService;

    private final ShipmentDTOMapper shipmentDTOMapper;

    @GetMapping
    public ResponseEntity<Page<ShipmentDTO>> getShipments(@RequestParam(defaultValue = "") String billNumber,
                                                          @RequestParam(defaultValue = "0") int pageNo,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                          @RequestParam(defaultValue = "asc") String order) {

        try {
            Page<ShipmentEntity> shipmentEntities = shipmentService.getShipments(billNumber, pageNo, pageSize, sortBy, order);
            Page<ShipmentDTO> shipmentDTOS = shipmentEntities.map(shipmentDTOMapper);
            return ResponseEntity.ok(shipmentDTOS);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipment(@PathVariable long id) {

        try {
            shipmentService.deleteShipment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
