package com.ya.pf.auditable.customer_payment;

import com.ya.pf.auditable.customer_payment.dto.CustomerPaymentDTO;
import com.ya.pf.auditable.customer_payment.dto.CustomerPaymentDTOMapper;
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
@RequestMapping("/payment/customer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerPaymentController {

	private final CustomerPaymentService customerPaymentService;

	private final CustomerPaymentDTOMapper customerPaymentDTOMapper;

	@GetMapping
	public ResponseEntity<Page<CustomerPaymentDTO>> getCustomerPayments(@RequestParam(defaultValue = "") String number,
	                                                                    @RequestParam(defaultValue = "0") int pageNo,
	                                                                    @RequestParam(defaultValue = "10") int pageSize,
	                                                                    @RequestParam(defaultValue = "id") String sortBy,
	                                                                    @RequestParam(defaultValue = "asc") String order) {

		Page<CustomerPaymentEntity> paymentEntities = customerPaymentService.getCustomerPayments(number, pageNo, pageSize, sortBy, order);
		Page<CustomerPaymentDTO> paymentDTOS = paymentEntities.map(customerPaymentDTOMapper);
		return ResponseEntity.ok(paymentDTOS);
	}

	@PostMapping
	public ResponseEntity<CustomerPaymentDTO> createCustomerPayment(@RequestBody CustomerPaymentEntity payment,
	                                                                @RequestParam(defaultValue = "false") boolean transferred,
	                                                                @RequestParam(defaultValue = "-1") long supplierId) {

		if (transferred && supplierId == -1) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		try {
			CustomerPaymentEntity customerPaymentEntity = customerPaymentService.createCustomerPayment(payment, transferred, supplierId);
			CustomerPaymentDTO customerPaymentDTO = customerPaymentDTOMapper.apply(customerPaymentEntity);
			return ResponseEntity.status(HttpStatus.CREATED).body(customerPaymentDTO);
		} catch (EntityExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{paymentId}")
	public ResponseEntity<Void> deleteCustomerPayment(@PathVariable long paymentId) {

		try {
			customerPaymentService.deleteCustomerPayment(paymentId);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
