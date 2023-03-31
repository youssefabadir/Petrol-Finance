package com.ya.pf.auditable.supplier;

import com.ya.pf.auditable.supplier.dto.SupplierDTO;
import com.ya.pf.auditable.supplier.dto.SupplierDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/supplier")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierController {

	private final SupplierService supplierService;

	private final SupplierDTOMapper supplierDTOMapper;

	@GetMapping
	public ResponseEntity<Page<SupplierDTO>> getSuppliers(@RequestParam(defaultValue = "") String name,
	                                                      @RequestParam(defaultValue = "0") int pageNo,
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

		try {
			SupplierEntity supplierEntity = supplierService.updateSupplier(supplier);
			SupplierDTO supplierDTO = supplierDTOMapper.apply(supplierEntity);
			return ResponseEntity.ok(supplierDTO);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSupplier(@PathVariable long id) {

		try {
			supplierService.deleteSupplier(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
