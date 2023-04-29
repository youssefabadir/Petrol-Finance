package com.ya.pf.auditable.payment_method;

import com.ya.pf.auditable.payment_method.dto.PaymentMethodDTO;
import com.ya.pf.auditable.payment_method.dto.PaymentMethodDTOMapper;
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
@RequestMapping("/payment-method")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentMethodController {

	private final PaymentMethodService paymentMethodService;

	private final PaymentMethodDTOMapper paymentMethodDTOMapper;

	@GetMapping
	public ResponseEntity<Page<PaymentMethodDTO>> getWayOfPayments(@RequestParam(defaultValue = "") String name,
	                                                               @RequestParam(defaultValue = "0") int pageNo,
	                                                               @RequestParam(defaultValue = "10") int pageSize,
	                                                               @RequestParam(defaultValue = "id") String sortBy,
	                                                               @RequestParam(defaultValue = "asc") String order) {

		Page<PaymentMethodEntity> wayOfPaymentEntities = paymentMethodService.getWayOfPayments(name, pageNo, pageSize, sortBy, order);
		Page<PaymentMethodDTO> wayOfPaymentDTOS = wayOfPaymentEntities.map(paymentMethodDTOMapper);
		return ResponseEntity.ok(wayOfPaymentDTOS);
	}

	@PostMapping
	public ResponseEntity<PaymentMethodDTO> createWayOfPayment(@RequestBody PaymentMethodEntity wayOfPayment) {

		try {
			PaymentMethodEntity paymentMethodEntity = paymentMethodService.createWayOfPayment(wayOfPayment);
			PaymentMethodDTO paymentMethodDTO = paymentMethodDTOMapper.apply(paymentMethodEntity);
			return ResponseEntity.status(HttpStatus.CREATED).body(paymentMethodDTO);
		} catch (EntityExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@PutMapping
	public ResponseEntity<PaymentMethodDTO> updateWayOfPayment(@RequestBody PaymentMethodEntity wayOfPayment) {

		try {
			PaymentMethodEntity paymentMethodEntity = paymentMethodService.updateWayOfPayment(wayOfPayment);
			PaymentMethodDTO paymentMethodDTO = paymentMethodDTOMapper.apply(paymentMethodEntity);
			return ResponseEntity.ok(paymentMethodDTO);
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
			paymentMethodService.deleteWayOfPayment(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
