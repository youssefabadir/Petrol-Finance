package com.ya.pf.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/transaction")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<TransactionEntity>> getTransactions(@RequestParam(defaultValue = "") String receiptNumber,
                                                                   @RequestParam(defaultValue = "0") int pageNo,
                                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                                   @RequestParam(defaultValue = "asc") String order) {

        Page<TransactionEntity> transactionEntities = transactionService.getTransactions(receiptNumber, pageNo, pageSize, sortBy, order);

        if (transactionEntities.hasContent()) {
            return ResponseEntity.ok(transactionEntities);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<TransactionEntity> createTransaction(@RequestBody TransactionEntity transaction) {

        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }

    @PutMapping
    public ResponseEntity<TransactionEntity> updateTransaction(@RequestBody TransactionEntity transaction) {

        return ResponseEntity.ok(transactionService.updateTransaction(transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable long id) {

        transactionService.deleteTransactionById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<String> getCustomerReport(@PathVariable long id,
                                                    @RequestParam(defaultValue = "") String receiptNumber,
                                                    @RequestParam(defaultValue = "0") int pageNo,
                                                    @RequestParam(defaultValue = "10") int pageSize,
                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String order) {

        try {
            return ResponseEntity.ok().body(transactionService.getCustomerReport(id, receiptNumber, pageNo, pageSize, sortBy, order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
