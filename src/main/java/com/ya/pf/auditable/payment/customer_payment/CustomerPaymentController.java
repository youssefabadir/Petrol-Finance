package com.ya.pf.auditable.payment.customer_payment;

import com.ya.pf.auditable.payment.customer_payment.dto.CustomerPaymentDTO;
import com.ya.pf.auditable.payment.customer_payment.dto.CustomerPaymentDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;

@Slf4j
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

        try {
            Page<CustomerPaymentEntity> customerPaymentEntities = customerPaymentService.getCustomerPayments(number.trim(), pageNo, pageSize, sortBy, order);
            Page<CustomerPaymentDTO> customerPaymentDTOS = customerPaymentEntities.map(customerPaymentDTOMapper);
            return ResponseEntity.ok(customerPaymentDTOS);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<CustomerPaymentDTO> createCustomerPayment(@RequestBody CustomerPaymentEntity payment,
                                                                    @RequestParam(defaultValue = "-1") long supplierId) {

        try {
            CustomerPaymentEntity customerPayment = customerPaymentService.createCustomerPayment(payment, supplierId);
            CustomerPaymentDTO customerPaymentDTO = customerPaymentDTOMapper.apply(customerPayment);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerPaymentDTO);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (MissingRequestValueException e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
