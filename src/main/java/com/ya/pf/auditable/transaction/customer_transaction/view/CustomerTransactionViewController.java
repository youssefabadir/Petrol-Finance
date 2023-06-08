package com.ya.pf.auditable.transaction.customer_transaction.view;

import com.ya.pf.auditable.transaction.customer_transaction.view.dto.CustomerTransactionViewDTO;
import com.ya.pf.auditable.transaction.customer_transaction.view.dto.CustomerTransactionViewDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/transaction/customer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerTransactionViewController {

    private final CustomerTransactionViewService customerTransactionViewService;

    private final CustomerTransactionViewDTOMapper customerTransactionViewDTOMapper;

    @GetMapping("/{customerId}")
    public ResponseEntity<Page<CustomerTransactionViewDTO>> getCustomerTransactions(@PathVariable long customerId,
                                                                                    @RequestParam(defaultValue = "0") int pageNo,
                                                                                    @RequestParam(defaultValue = "10") int pageSize,
                                                                                    @RequestParam(defaultValue = "date") String sortBy,
                                                                                    @RequestParam(defaultValue = "asc") String order,
                                                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate start,
                                                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate end) {

        try {
            Page<CustomerTransactionView> transactionViews = customerTransactionViewService.getCustomerTransaction(customerId,
                                                                                                                   pageNo,
                                                                                                                   pageSize,
                                                                                                                   sortBy,
                                                                                                                   order,
                                                                                                                   start,
                                                                                                                   end);
            Page<CustomerTransactionViewDTO> customerTransactionViewDTO = transactionViews.map(customerTransactionViewDTOMapper);
            return ResponseEntity.ok(customerTransactionViewDTO);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
