package com.ya.pf.auditable.payment.customer_payment;

import com.ya.pf.auditable.payment.customer_payment.dto.CustomerPaymentDTO;
import com.ya.pf.auditable.payment.customer_payment.dto.CustomerPaymentDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment/customer")
@RequiredArgsConstructor
public class CustomerPaymentController {

    private final CustomerPaymentService customerPaymentService;
    private final CustomerPaymentDTOMapper customerPaymentDTOMapper;

    @GetMapping
    public ResponseEntity<Page<CustomerPaymentDTO>> getCustomerPayments(@RequestParam(defaultValue = "") String number,
                                                                        @RequestParam(defaultValue = "0") int pageNo,
                                                                        @RequestParam(defaultValue = "10") int pageSize,
                                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                                        @RequestParam(defaultValue = "asc") String order) {
        Page<CustomerPaymentEntity> customerPaymentEntities = customerPaymentService.getCustomerPayments(number.trim(),
                                                                                                         pageNo,
                                                                                                         pageSize,
                                                                                                         sortBy,
                                                                                                         order);
        Page<CustomerPaymentDTO> customerPaymentDTOS = customerPaymentEntities.map(customerPaymentDTOMapper);
        return ResponseEntity.ok(customerPaymentDTOS);
    }

    @PostMapping
    public ResponseEntity<CustomerPaymentDTO> createCustomerPayment(@RequestBody CustomerPaymentEntity payment,
                                                                    @RequestParam(defaultValue = "-1") long supplierId) {
        CustomerPaymentEntity customerPayment = customerPaymentService.createCustomerPayment(payment, supplierId);
        CustomerPaymentDTO customerPaymentDTO = customerPaymentDTOMapper.apply(customerPayment);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerPaymentDTO);
    }

    @PutMapping
    public ResponseEntity<CustomerPaymentDTO> updateCustomerPayment(@RequestBody CustomerPaymentEntity payment,
                                                                    @RequestParam(defaultValue = "-1") long supplierId) {
        CustomerPaymentEntity customerPayment = customerPaymentService.updateCustomerPayment(payment, supplierId);
        CustomerPaymentDTO customerPaymentDTO = customerPaymentDTOMapper.apply(customerPayment);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerPaymentDTO);
    }

}
