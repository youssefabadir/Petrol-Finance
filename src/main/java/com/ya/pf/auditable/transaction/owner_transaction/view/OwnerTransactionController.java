package com.ya.pf.auditable.transaction.owner_transaction.view;

import com.ya.pf.auditable.transaction.owner_transaction.view.dto.OwnerTransactionViewDTO;
import com.ya.pf.auditable.transaction.owner_transaction.view.dto.OwnerTransactionViewDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/transaction/owner")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerTransactionController {

    private final OwnerTransactionViewService ownerTransactionViewService;

    private final OwnerTransactionViewDTOMapper ownerTransactionViewDTOMapper;

    @GetMapping("/{supplierId}")
    public ResponseEntity<Page<OwnerTransactionViewDTO>> getOwnerTransactions(@PathVariable long supplierId,
                                                                              @RequestParam(defaultValue = "0") int pageNo,
                                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                                              @RequestParam(defaultValue = "date") String sortBy,
                                                                              @RequestParam(defaultValue = "asc") String order,
                                                                              @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate start,
                                                                              @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate end) {

        try {
            Page<OwnerTransactionView> transactionViews = ownerTransactionViewService.getSupplierTransaction(supplierId,
                                                                                                             pageNo,
                                                                                                             pageSize,
                                                                                                             sortBy,
                                                                                                             order,
                                                                                                             start,
                                                                                                             end);
            Page<OwnerTransactionViewDTO> ownerTransactionViewDTOS = transactionViews.map(ownerTransactionViewDTOMapper);
            return ResponseEntity.ok(ownerTransactionViewDTOS);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
