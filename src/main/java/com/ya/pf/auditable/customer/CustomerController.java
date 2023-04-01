package com.ya.pf.auditable.customer;

import com.ya.pf.auditable.customer.dto.CustomerDTO;
import com.ya.pf.auditable.customer.dto.CustomerDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerController {

	private final CustomerService customerService;

	private final CustomerDTOMapper customerDTOMapper;

	@GetMapping
	public ResponseEntity<Page<CustomerDTO>> getCustomers(@RequestParam(defaultValue = "") String name,
	                                                      @RequestParam(defaultValue = "0") int pageNo,
	                                                      @RequestParam(defaultValue = "10") int pageSize,
	                                                      @RequestParam(defaultValue = "id") String sortBy,
	                                                      @RequestParam(defaultValue = "asc") String order) {

		Page<CustomerEntity> customerEntities = customerService.getCustomers(name, pageNo, pageSize, sortBy, order);
		Page<CustomerDTO> customerDTOS = customerEntities.map(customerDTOMapper);
		return ResponseEntity.ok(customerDTOS);
	}

	@PostMapping
	public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerEntity customer) {

		CustomerEntity customerEntity = customerService.createCustomer(customer);
		CustomerDTO customerDTO = customerDTOMapper.apply(customerEntity);
		return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
	}

	@PutMapping
	public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerEntity customer) {

		try {
			CustomerEntity customerEntity = customerService.updateCustomer(customer);
			CustomerDTO customerDTO = customerDTOMapper.apply(customerEntity);
			return ResponseEntity.ok(customerDTO);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {

		try {
			customerService.deleteCustomer(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/search")
	public ResponseEntity<List<CustomerDTO>> searchCustomer(@RequestParam(defaultValue = "") String name) {

		if (name.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		List<CustomerEntity> customerEntities = customerService.searchCustomer(name);
		List<CustomerDTO> customerDTOS = customerEntities.stream().map(customerDTOMapper).toList();
		return ResponseEntity.ok(customerDTOS);
	}

}
