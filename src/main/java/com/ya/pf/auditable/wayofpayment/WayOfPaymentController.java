package com.ya.pf.auditable.wayofpayment;

import com.ya.pf.auditable.wayofpayment.dto.WayOfPaymentDTO;
import com.ya.pf.auditable.wayofpayment.dto.WayOfPaymentDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/wayOfPayment")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WayOfPaymentController {

	private final WayOfPaymentService wayOfPaymentService;

	private final WayOfPaymentDTOMapper wayOfPaymentDTOMapper;

	@GetMapping
	public ResponseEntity<Page<WayOfPaymentDTO>> getWayOfPayments(@RequestParam(defaultValue = "") String name,
	                                                              @RequestParam(defaultValue = "0") int pageNo,
	                                                              @RequestParam(defaultValue = "10") int pageSize,
	                                                              @RequestParam(defaultValue = "id") String sortBy,
	                                                              @RequestParam(defaultValue = "asc") String order) {

		Page<WayOfPaymentEntity> wayOfPaymentEntities = wayOfPaymentService.getWayOfPayments(name, pageNo, pageSize, sortBy, order);
		Page<WayOfPaymentDTO> wayOfPaymentDTOS = wayOfPaymentEntities.map(wayOfPaymentDTOMapper);
		return ResponseEntity.ok(wayOfPaymentDTOS);
	}

	@PostMapping
	public ResponseEntity<WayOfPaymentDTO> createWayOfPayment(@RequestBody WayOfPaymentEntity wayOfPayment) {

		try {
			WayOfPaymentEntity wayOfPaymentEntity = wayOfPaymentService.createWayOfPayment(wayOfPayment);
			WayOfPaymentDTO wayOfPaymentDTO = wayOfPaymentDTOMapper.apply(wayOfPaymentEntity);
			return ResponseEntity.status(HttpStatus.CREATED).body(wayOfPaymentDTO);
		} catch (EntityExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@PutMapping
	public ResponseEntity<WayOfPaymentDTO> updateWayOfPayment(@RequestBody WayOfPaymentEntity wayOfPayment) {

		try {
			WayOfPaymentEntity wayOfPaymentEntity = wayOfPaymentService.updateWayOfPayment(wayOfPayment);
			WayOfPaymentDTO wayOfPaymentDTO = wayOfPaymentDTOMapper.apply(wayOfPaymentEntity);
			return ResponseEntity.ok(wayOfPaymentDTO);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (EntityExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePayment(@PathVariable long id) {

		try {
			wayOfPaymentService.deleteWayOfPayment(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
