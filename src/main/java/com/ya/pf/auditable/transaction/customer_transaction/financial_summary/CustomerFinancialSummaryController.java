package com.ya.pf.auditable.transaction.customer_transaction.financial_summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/financial-summary/customer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerFinancialSummaryController {

    private final CustomerFinancialSummaryService customerFinancialSummaryService;

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerFinancialSummary> getCustomerTotal(@PathVariable long customerId,
                                                                     @RequestParam(required = false) Integer productId,
                                                                     @RequestParam(required = false) Integer paymentMethodId,
                                                                     @RequestParam() @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate start,
                                                                     @RequestParam() @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate end) {

        try {
            return ResponseEntity.ok(customerFinancialSummaryService.getCustomerFinancialSummary(customerId, productId, paymentMethodId, start, end));
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
