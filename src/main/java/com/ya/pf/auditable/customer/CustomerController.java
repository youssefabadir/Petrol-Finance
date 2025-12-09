package com.ya.pf.auditable.customer;

import com.ya.pf.auditable.customer.dto.CustomerDTO;
import com.ya.pf.auditable.customer.dto.CustomerDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/customer")
@RequiredArgsConstructor
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
        CustomerEntity customerEntity = customerService.updateCustomer(customer);
        CustomerDTO customerDTO = customerDTOMapper.apply(customerEntity);
        return ResponseEntity.ok(customerDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO>> searchCustomer(@RequestParam(defaultValue = "") String name) {
        if (name.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<CustomerEntity> customerEntities = customerService.searchCustomer(name);
        List<CustomerDTO> customerDTOS = customerEntities.stream().map(customerDTOMapper).toList();
        return ResponseEntity.ok(customerDTOS);
    }

}
