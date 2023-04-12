package com.ya.pf.auditable.owner_payment;

import com.ya.pf.auditable.owner_payment.dto.OwnerPaymentDTO;
import com.ya.pf.auditable.owner_payment.dto.OwnerPaymentDTOMapper;
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
@RequestMapping("/payment/owner")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerPaymentController {

	private final OwnerPaymentService ownerPaymentService;

	private final OwnerPaymentDTOMapper ownerPaymentDTOMapper;

	@GetMapping
	public ResponseEntity<Page<OwnerPaymentDTO>> getOwnerPayments(@RequestParam(defaultValue = "") String number,
	                                                              @RequestParam(defaultValue = "0") int pageNo,
	                                                              @RequestParam(defaultValue = "10") int pageSize,
	                                                              @RequestParam(defaultValue = "id") String sortBy,
	                                                              @RequestParam(defaultValue = "asc") String order) {

		Page<OwnerPaymentEntity> paymentEntities = ownerPaymentService.getOwnerPayments(number, pageNo, pageSize, sortBy, order);
		Page<OwnerPaymentDTO> paymentDTOS = paymentEntities.map(ownerPaymentDTOMapper);
		return ResponseEntity.ok(paymentDTOS);
	}

	@PostMapping
	public ResponseEntity<OwnerPaymentDTO> createOwnerPayment(@RequestBody OwnerPaymentEntity payment) {

		try {
			OwnerPaymentEntity customerPaymentEntity = ownerPaymentService.createOwnerPayment(payment);
			OwnerPaymentDTO customerPaymentDTO = ownerPaymentDTOMapper.apply(customerPaymentEntity);
			return ResponseEntity.status(HttpStatus.CREATED).body(customerPaymentDTO);
		} catch (EntityExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOwnerPayment(@PathVariable long id) {

		try {
			ownerPaymentService.deleteOwnerPayment(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
