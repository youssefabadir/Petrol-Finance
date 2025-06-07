package com.ya.pf.auditable.supplier;

import com.ya.pf.auditable.supplier.dto.SupplierDTO;
import com.ya.pf.auditable.supplier.dto.SupplierDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/supplier")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierController {

    private final SupplierService supplierService;

    private final SupplierDTOMapper supplierDTOMapper;

    @GetMapping
    public ResponseEntity<Page<SupplierDTO>> getSuppliers(@RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "0") int pageNo,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                          @RequestParam(defaultValue = "asc") String order) {
        Page<SupplierEntity> supplierEntities = supplierService.getSuppliers(name, pageNo, pageSize, sortBy, order);
        Page<SupplierDTO> supplierDTOS = supplierEntities.map(supplierDTOMapper);
        return ResponseEntity.ok(supplierDTOS);
    }

    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@RequestBody SupplierEntity supplier) {
        SupplierEntity supplierEntity = supplierService.createSupplier(supplier);
        SupplierDTO supplierDTO = supplierDTOMapper.apply(supplierEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierDTO);
    }

    @PutMapping
    public ResponseEntity<SupplierDTO> updateSupplier(@RequestBody SupplierEntity supplier) {
        SupplierEntity supplierEntity = supplierService.updateSupplier(supplier);
        SupplierDTO supplierDTO = supplierDTOMapper.apply(supplierEntity);
        return ResponseEntity.ok(supplierDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<SupplierDTO>> searchSupplier(@RequestParam(defaultValue = "") String name) {

        if (name.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<SupplierEntity> supplierEntities = supplierService.searchSupplier(name);
        List<SupplierDTO> supplierDTOS = supplierEntities.stream().map(supplierDTOMapper).toList();
        return ResponseEntity.ok(supplierDTOS);
    }

}
