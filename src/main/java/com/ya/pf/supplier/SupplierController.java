package com.ya.pf.supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<String> getSuppliers(@RequestParam(defaultValue = "") String name,
                                               @RequestParam(defaultValue = "0") int pageNo,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               @RequestParam(defaultValue = "id") String sortBy,
                                               @RequestParam(defaultValue = "asc") String order) {

        Page<SupplierEntity> supplierEntities = supplierService.getSuppliers(name, pageNo, pageSize, sortBy, order);

        if (supplierEntities.hasContent()) {
            try {
                return ResponseEntity.ok().body(new ObjectMapper().writeValueAsString(supplierEntities));
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<SupplierEntity> createSupplier(@RequestBody SupplierEntity supplier) {

        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.createSupplier(supplier));
    }

    @PutMapping
    public ResponseEntity<SupplierEntity> updateSupplier(@RequestBody SupplierEntity supplier) {

        return ResponseEntity.ok().body(supplierService.updateSupplier(supplier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable long id) {

        supplierService.deleteSupplier(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
