package com.ya.pf.auditable.truck;

import com.ya.pf.auditable.truck.dto.TruckDTO;
import com.ya.pf.auditable.truck.dto.TruckDTOMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/truck")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TruckController {

    private final TruckService truckService;

    private final TruckDTOMapper truckDTOMapper;

    @GetMapping
    public ResponseEntity<Page<TruckDTO>> getTrucks(@RequestParam(defaultValue = "") String number,
                                                    @RequestParam(defaultValue = "0") int pageNo,
                                                    @RequestParam(defaultValue = "10") int pageSize,
                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String order) {

        try {
            Page<TruckEntity> truckEntities = truckService.getTrucks(number, pageNo, pageSize, sortBy, order);
            Page<TruckDTO> truckDTOS = truckEntities.map(truckDTOMapper);
            return ResponseEntity.ok(truckDTOS);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<TruckDTO> createTruck(@RequestBody TruckEntity truck) {

        try {
            TruckEntity truckEntity = truckService.createTruck(truck);
            TruckDTO truckDTO = truckDTOMapper.apply(truckEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(truckDTO);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<TruckDTO> updateTruck(@RequestBody TruckEntity truck) {

        try {
            TruckEntity truckEntity = truckService.updateTruck(truck);
            TruckDTO truckDTO = truckDTOMapper.apply(truckEntity);
            return ResponseEntity.ok(truckDTO);
        } catch (EntityNotFoundException e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTruck(@PathVariable long id) {

        try {
            truckService.deleteTruck(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<TruckDTO>> searchTruck(@RequestParam(defaultValue = "") String number) {

        if (number.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            List<TruckEntity> truckEntities = truckService.searchTruck(number);
            List<TruckDTO> truckDTOS = truckEntities.stream().map(truckDTOMapper).toList();
            return ResponseEntity.ok(truckDTOS);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
