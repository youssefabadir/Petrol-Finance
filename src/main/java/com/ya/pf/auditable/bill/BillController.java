package com.ya.pf.auditable.bill;

import com.ya.pf.auditable.bill.dto.BillDTO;
import com.ya.pf.auditable.bill.dto.BillDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/bill")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
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

        try {
            Page<BillEntity> transactionEntities = billService.getBills(number, pageNo, pageSize, sortBy, order, start, end);
            Page<BillDTO> transactionDTOS = transactionEntities.map(billDTOMapper);
            return ResponseEntity.ok(transactionDTOS);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<BillDTO> createBill(@RequestBody BillEntity bill,
                                              @RequestParam long truckId) {

        try {
            BillEntity billEntity = billService.createBill(bill, truckId);
            BillDTO billDTO = billDTOMapper.apply(billEntity);
            return ResponseEntity.ok(billDTO);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<BillDTO> updateBill(@RequestBody BillEntity bill,
                                              @RequestParam long truckId) {

        try {
            BillEntity billEntity = billService.updateBill(bill, truckId);
            BillDTO billDTO = billDTOMapper.apply(billEntity);
            return ResponseEntity.ok(billDTO);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
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
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
