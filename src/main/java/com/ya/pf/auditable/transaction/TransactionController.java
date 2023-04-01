package com.ya.pf.auditable.transaction;

import com.ya.pf.auditable.transaction.dto.TransactionDTO;
import com.ya.pf.auditable.transaction.dto.TransactionDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/transaction")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionController {

	private final TransactionService transactionService;

	private final TransactionDTOMapper transactionDTOMapper;

	@GetMapping
	public ResponseEntity<Page<TransactionDTO>> getTransactions(@RequestParam(defaultValue = "") String receiptNumber,
	                                                            @RequestParam(defaultValue = "0") int pageNo,
	                                                            @RequestParam(defaultValue = "10") int pageSize,
	                                                            @RequestParam(defaultValue = "id") String sortBy,
	                                                            @RequestParam(defaultValue = "asc") String order,
	                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start,
	                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate end) {

		Page<TransactionEntity> transactionEntities = transactionService.getTransactions(receiptNumber, pageNo, pageSize, sortBy, order, start, end);
		Page<TransactionDTO> transactionDTOS = transactionEntities.map(transactionDTOMapper);
		return ResponseEntity.ok(transactionDTOS);
	}

	@PostMapping
	public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionEntity transaction) {

		TransactionEntity transactionEntity = transactionService.createTransaction(transaction);
		TransactionDTO transactionDTO = transactionDTOMapper.apply(transactionEntity);
		return ResponseEntity.ok(transactionDTO);
	}

	@PutMapping
	public ResponseEntity<TransactionDTO> updateTransaction(@RequestBody TransactionEntity transaction) {

		try {
			TransactionEntity transactionEntity = transactionService.updateTransaction(transaction);
			TransactionDTO transactionDTO = transactionDTOMapper.apply(transactionEntity);
			return ResponseEntity.ok(transactionDTO);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTransaction(@PathVariable long id) {

		try {
			transactionService.deleteTransactionById(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/customer/{id}")
	public ResponseEntity<CustomerReport> getCustomerReport(@PathVariable long id,
	                                                        @RequestParam(defaultValue = "") String receiptNumber,
	                                                        @RequestParam(defaultValue = "0") int pageNo,
	                                                        @RequestParam(defaultValue = "10") int pageSize,
	                                                        @RequestParam(defaultValue = "id") String sortBy,
	                                                        @RequestParam(defaultValue = "asc") String order,
	                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start,
	                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate end) {

		try {
			CustomerReport customerReport = transactionService.getCustomerReport(id, receiptNumber, pageNo, pageSize, sortBy, order, start, end);
			return ResponseEntity.ok(customerReport);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
