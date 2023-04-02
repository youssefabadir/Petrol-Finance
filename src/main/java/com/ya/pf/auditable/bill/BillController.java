package com.ya.pf.auditable.bill;

import com.ya.pf.auditable.bill.dto.BillDTO;
import com.ya.pf.auditable.bill.dto.BillDTOMapper;
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
@RequestMapping("/bill")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BillController {

	private final BillService billService;

	private final BillDTOMapper billDTOMapper;

	@GetMapping
	public ResponseEntity<Page<BillDTO>> getBills(@RequestParam(defaultValue = "") String receiptNumber,
	                                              @RequestParam(defaultValue = "0") int pageNo,
	                                              @RequestParam(defaultValue = "10") int pageSize,
	                                              @RequestParam(defaultValue = "id") String sortBy,
	                                              @RequestParam(defaultValue = "asc") String order,
	                                              @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start,
	                                              @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate end) {

		Page<BillEntity> transactionEntities = billService.getBills(receiptNumber, pageNo, pageSize, sortBy, order, start, end);
		Page<BillDTO> transactionDTOS = transactionEntities.map(billDTOMapper);
		return ResponseEntity.ok(transactionDTOS);
	}

	@PostMapping
	public ResponseEntity<BillDTO> createBill(@RequestBody BillEntity transaction) {

		BillEntity billEntity = billService.createBill(transaction);
		BillDTO billDTO = billDTOMapper.apply(billEntity);
		return ResponseEntity.ok(billDTO);
	}

	@PutMapping
	public ResponseEntity<BillDTO> updateBill(@RequestBody BillEntity transaction) {

		try {
			BillEntity billEntity = billService.updateBill(transaction);
			BillDTO billDTO = billDTOMapper.apply(billEntity);
			return ResponseEntity.ok(billDTO);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBill(@PathVariable long id) {

		try {
			billService.deleteBill(id);
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
			CustomerReport customerReport = billService.getCustomerReport(id, receiptNumber, pageNo, pageSize, sortBy, order, start, end);
			return ResponseEntity.ok(customerReport);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
