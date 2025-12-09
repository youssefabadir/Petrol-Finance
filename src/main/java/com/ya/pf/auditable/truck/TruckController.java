package com.ya.pf.auditable.truck;

import com.ya.pf.auditable.truck.dto.TruckDTO;
import com.ya.pf.auditable.truck.dto.TruckDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/truck")
@RequiredArgsConstructor
public class TruckController {

    private final TruckService truckService;

    private final TruckDTOMapper truckDTOMapper;

    @GetMapping
    public ResponseEntity<Page<TruckDTO>> getTrucks(@RequestParam(defaultValue = "") String number,
                                                    @RequestParam(defaultValue = "0") int pageNo,
                                                    @RequestParam(defaultValue = "10") int pageSize,
                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String order) {

        Page<TruckEntity> truckEntities = truckService.getTrucks(number, pageNo, pageSize, sortBy, order);
        Page<TruckDTO> truckDTOS = truckEntities.map(truckDTOMapper);
        return ResponseEntity.ok(truckDTOS);
    }

    @PostMapping
    public ResponseEntity<TruckDTO> createTruck(@RequestBody TruckEntity truck) {
        TruckEntity truckEntity = truckService.createTruck(truck);
        TruckDTO truckDTO = truckDTOMapper.apply(truckEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(truckDTO);
    }

    @PutMapping
    public ResponseEntity<TruckDTO> updateTruck(@RequestBody TruckEntity truck) {
        TruckEntity truckEntity = truckService.updateTruck(truck);
        TruckDTO truckDTO = truckDTOMapper.apply(truckEntity);
        return ResponseEntity.ok(truckDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTruck(@PathVariable long id) {
        truckService.deleteTruck(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<TruckDTO>> searchTruck(@RequestParam(defaultValue = "") String number) {

        if (number.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<TruckEntity> truckEntities = truckService.searchTruck(number);
        List<TruckDTO> truckDTOS = truckEntities.stream().map(truckDTOMapper).toList();
        return ResponseEntity.ok(truckDTOS);
    }

}
