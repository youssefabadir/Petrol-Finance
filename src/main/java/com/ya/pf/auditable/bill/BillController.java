package com.ya.pf.auditable.bill;

import com.ya.pf.auditable.bill.dto.BillDTO;
import com.ya.pf.auditable.bill.dto.BillDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    private final BillDTOMapper billDTOMapper;

    @GetMapping
    public ResponseEntity<Page<BillDTO>> getBills(@RequestParam(defaultValue = "") String number,
                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String order,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate start,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate end) {
        Page<BillEntity> transactionEntities = billService.getBills(number,
                                                                    pageNo,
                                                                    pageSize,
                                                                    sortBy,
                                                                    order,
                                                                    start,
                                                                    end);
        Page<BillDTO> transactionDTOS = transactionEntities.map(billDTOMapper);
        return ResponseEntity.ok(transactionDTOS);
    }

    @PostMapping
    public ResponseEntity<BillDTO> createBill(@RequestBody BillEntity bill, @RequestParam long truckId) {
        BillEntity billEntity = billService.createBill(bill, truckId);
        BillDTO billDTO = billDTOMapper.apply(billEntity);
        return ResponseEntity.ok(billDTO);
    }

    @PutMapping
    public ResponseEntity<BillDTO> updateBill(@RequestBody BillEntity bill, @RequestParam long truckId) {
        BillEntity billEntity = billService.updateBill(bill, truckId);
        BillDTO billDTO = billDTOMapper.apply(billEntity);
        return ResponseEntity.ok(billDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }

}
