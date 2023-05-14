package com.ya.pf.auditable.driver;

import com.ya.pf.auditable.driver.dto.DriverDTO;
import com.ya.pf.auditable.driver.dto.DriverDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/driver")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DriverController {

    private final DriverService driverService;

    private final DriverDTOMapper driverDTOMapper;

    @GetMapping
    public ResponseEntity<Page<DriverDTO>> getDrivers(@RequestParam(defaultValue = "") String name,
                                                      @RequestParam(defaultValue = "0") int pageNo,
                                                      @RequestParam(defaultValue = "10") int pageSize,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String order) {

        try {
            Page<DriverEntity> driverEntities = driverService.getDrivers(name, pageNo, pageSize, sortBy, order);
            Page<DriverDTO> driverDTOS = driverEntities.map(driverDTOMapper);
            return ResponseEntity.ok(driverDTOS);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@RequestBody DriverEntity driver) {

        try {
            DriverEntity driverEntity = driverService.createDriver(driver);
            DriverDTO driverDTO = driverDTOMapper.apply(driverEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(driverDTO);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<DriverDTO> updateDriver(@RequestBody DriverEntity driver) {

        try {
            DriverEntity driverEntity = driverService.updateDriver(driver);
            DriverDTO driverDTO = driverDTOMapper.apply(driverEntity);
            return ResponseEntity.ok(driverDTO);
        } catch (EntityNotFoundException e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable long id) {

        try {
            driverService.deleteDriver(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<DriverDTO>> searchDriver(@RequestParam(defaultValue = "") String name) {

        if (name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            List<DriverEntity> driverEntities = driverService.searchDriver(name);
            List<DriverDTO> driverDTOS = driverEntities.stream().map(driverDTOMapper).toList();
            return ResponseEntity.ok(driverDTOS);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
