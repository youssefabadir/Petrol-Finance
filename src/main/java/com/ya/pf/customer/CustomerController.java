package com.ya.pf.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<String> getCustomers(@RequestParam(defaultValue = "") String name,
                                               @RequestParam(defaultValue = "0") int pageNo,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               @RequestParam(defaultValue = "id") String sortBy,
                                               @RequestParam(defaultValue = "asc") String order) {

        Page<CustomerEntity> customerEntities = customerService.getCustomers(name, pageNo, pageSize, sortBy, order);

        if (customerEntities.hasContent()) {
            try {
                return ResponseEntity.ok().body(new ObjectMapper().writeValueAsString(customerEntities));
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<CustomerEntity> createCustomer(@RequestBody CustomerEntity customer) {

        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
    }

    @PutMapping
    public ResponseEntity<CustomerEntity> updateCustomer(@RequestBody CustomerEntity customer) {

        return ResponseEntity.ok().body(customerService.updateCustomer(customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {

        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

}
