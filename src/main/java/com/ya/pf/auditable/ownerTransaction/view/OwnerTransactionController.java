package com.ya.pf.auditable.ownerTransaction.view;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/transaction/owner")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerTransactionController {

	private final OwnerTransactionViewService ownerTransactionViewService;

	@GetMapping("/{supplierId}")
	public ResponseEntity<Page<OwnerTransactionView>> getOwnerTransactions(@PathVariable long supplierId,
	                                                                       @RequestParam(defaultValue = "0") int pageNo,
	                                                                       @RequestParam(defaultValue = "10") int pageSize,
	                                                                       @RequestParam(defaultValue = "date") String sortBy,
	                                                                       @RequestParam(defaultValue = "asc") String order,
	                                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate start,
	                                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate end) {

		Page<OwnerTransactionView> transactionViews = ownerTransactionViewService.getSupplierTransaction(supplierId, pageNo, pageSize, sortBy, order, start, end);
		return ResponseEntity.ok(transactionViews);
	}

}
