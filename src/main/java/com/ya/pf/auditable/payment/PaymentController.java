package com.ya.pf.auditable.payment;

import com.ya.pf.auditable.payment.dto.PaymentDTO;
import com.ya.pf.auditable.payment.dto.PaymentDTOMapper;
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
@RequestMapping("/payment")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentController {

	private final PaymentService paymentService;

	private final PaymentDTOMapper paymentDTOMapper;

	@GetMapping
	public ResponseEntity<Page<PaymentDTO>> getPayments(@RequestParam(defaultValue = "") String receiptNumber,
	                                                    @RequestParam(defaultValue = "0") int pageNo,
	                                                    @RequestParam(defaultValue = "10") int pageSize,
	                                                    @RequestParam(defaultValue = "id") String sortBy,
	                                                    @RequestParam(defaultValue = "asc") String order) {

		Page<PaymentEntity> paymentEntities = paymentService.getPayments(receiptNumber, pageNo, pageSize, sortBy, order);
		Page<PaymentDTO> paymentDTOS = paymentEntities.map(paymentDTOMapper);
		return ResponseEntity.ok(paymentDTOS);
	}

	@PostMapping
	public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentEntity payment) {

		try {
			PaymentEntity paymentEntity = paymentService.createPayment(payment);
			PaymentDTO paymentDTO = paymentDTOMapper.apply(paymentEntity);
			return ResponseEntity.status(HttpStatus.CREATED).body(paymentDTO);
		} catch (EntityExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePayment(@PathVariable long id) {

		try {
			paymentService.deletePayment(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
