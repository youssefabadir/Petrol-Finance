package com.ya.pf.auditable.transaction.customer_transaction.financial_summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/financial-summary/customer")
@RequiredArgsConstructor
public class CustomerFinancialSummaryController {

    private final CustomerFinancialSummaryService customerFinancialSummaryService;

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerFinancialSummary> getCustomerTotal(@PathVariable long customerId,
                                                                     @RequestParam(required = false) Integer productId,
                                                                     @RequestParam(required = false) Integer paymentMethodId,
                                                                     @RequestParam() @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate start,
                                                                     @RequestParam() @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate end) {
        return ResponseEntity.ok(customerFinancialSummaryService.getCustomerFinancialSummary(customerId,
                                                                                             productId,
                                                                                             paymentMethodId,
                                                                                             start,
                                                                                             end));

    }

}
