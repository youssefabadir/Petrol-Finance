package com.ya.pf.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<String> getTransactions(@RequestParam(defaultValue = "-1") int receiptNumber,
                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String order) {

        Page<TransactionEntity> transactionEntities = transactionService.getTransactions(receiptNumber, pageNo, pageSize, sortBy, order);

        if (transactionEntities.hasContent()) {
            try {
                return ResponseEntity.ok().body(new ObjectMapper().writeValueAsString(transactionEntities));
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestBody TransactionEntity transaction) {

        return ResponseEntity.ok().body(transactionService.createTransaction(transaction).toString());
    }

    @PutMapping
    public ResponseEntity<String> updateTransaction(@RequestBody TransactionEntity transaction) {

        transactionService.updateTransaction(transaction);
        return ResponseEntity.ok().body("updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable long id) {

        transactionService.deleteTransactionById(id);
        return ResponseEntity.ok().body("deleted");
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<String> getCustomerReport(@PathVariable long id,
                                                    @RequestParam(defaultValue = "0") int pageNo,
                                                    @RequestParam(defaultValue = "10") int pageSize) {

        try {
            return ResponseEntity.ok().body(transactionService.getCustomerReport(id, pageNo, pageSize));
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
