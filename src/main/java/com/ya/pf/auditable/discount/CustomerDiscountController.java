package com.ya.pf.auditable.discount;

import com.ya.pf.auditable.discount.dto.CustomerDiscountDTO;
import com.ya.pf.auditable.discount.dto.CustomerDiscountDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/discount")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerDiscountController {

	private final CustomerDiscountService customerDiscountService;

	private final CustomerDiscountDTOMapper customerDiscountDTOMapper;

	@GetMapping
	public ResponseEntity<Page<CustomerDiscountDTO>> getCustomersDiscount(@RequestParam(defaultValue = "0") int pageNo,
	                                                                      @RequestParam(defaultValue = "10") int pageSize,
	                                                                      @RequestParam(defaultValue = "id") String sortBy,
	                                                                      @RequestParam(defaultValue = "asc") String order) {

		Page<CustomerDiscountEntity> customerDiscountEntities = customerDiscountService.getCustomersDiscount(pageNo, pageSize, sortBy, order);
		Page<CustomerDiscountDTO> customerDiscountDTOS = customerDiscountEntities.map(customerDiscountDTOMapper);
		return ResponseEntity.ok(customerDiscountDTOS);
	}

	@PostMapping
	public ResponseEntity<CustomerDiscountDTO> createCustomerDiscount(@RequestBody CustomerDiscountEntity customerDiscount) {

		CustomerDiscountEntity customerDiscountEntity = customerDiscountService.createCustomerDiscount(customerDiscount);
		CustomerDiscountDTO customerDiscountDTO = customerDiscountDTOMapper.apply(customerDiscountEntity);
		return ResponseEntity.status(HttpStatus.CREATED).body(customerDiscountDTO);
	}

	@PutMapping
	public ResponseEntity<CustomerDiscountDTO> updateCustomerDiscount(@RequestBody CustomerDiscountEntity customerDiscount) {

		try {
			CustomerDiscountEntity customerDiscountEntity = customerDiscountService.updateCustomerDiscount(customerDiscount);
			CustomerDiscountDTO customerDiscountDTO = customerDiscountDTOMapper.apply(customerDiscountEntity);
			return ResponseEntity.ok(customerDiscountDTO);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomerDiscount(@PathVariable long id) {

		try {
			customerDiscountService.deleteCustomerDiscount(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
