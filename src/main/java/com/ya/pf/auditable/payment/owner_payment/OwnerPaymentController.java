package com.ya.pf.auditable.payment.owner_payment;

import com.ya.pf.auditable.payment.owner_payment.dto.OwnerPaymentDTO;
import com.ya.pf.auditable.payment.owner_payment.dto.OwnerPaymentDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment/owner")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerPaymentController {

    private final OwnerPaymentServiceImpl ownerPaymentService;

    private final OwnerPaymentDTOMapper ownerPaymentDTOMapper;

    @GetMapping
    public ResponseEntity<Page<OwnerPaymentDTO>> getOwnerPayments(@RequestParam(defaultValue = "") String number,
                                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                  @RequestParam(defaultValue = "asc") String order) {

        try {
            Page<OwnerPaymentEntity> ownerPaymentEntities = ownerPaymentService.getOwnerPayments(number.trim(), pageNo, pageSize, sortBy, order);
            Page<OwnerPaymentDTO> ownerPaymentDTOS = ownerPaymentEntities.map(ownerPaymentDTOMapper);
            return ResponseEntity.ok(ownerPaymentDTOS);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<OwnerPaymentDTO> createOwnerPayment(@RequestBody OwnerPaymentEntity payment) {

        try {
            OwnerPaymentEntity ownerPayment = ownerPaymentService.createOwnerPayment(payment);
            OwnerPaymentDTO ownerPaymentDTO = ownerPaymentDTOMapper.apply(ownerPayment);
            return ResponseEntity.status(HttpStatus.CREATED).body(ownerPaymentDTO);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (MissingRequestValueException e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
