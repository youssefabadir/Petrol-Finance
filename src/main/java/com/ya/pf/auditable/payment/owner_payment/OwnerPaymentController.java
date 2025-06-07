package com.ya.pf.auditable.payment.owner_payment;

import com.ya.pf.auditable.payment.owner_payment.dto.OwnerPaymentDTO;
import com.ya.pf.auditable.payment.owner_payment.dto.OwnerPaymentDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment/owner")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerPaymentController {

    private final OwnerPaymentService ownerPaymentService;

    private final OwnerPaymentDTOMapper ownerPaymentDTOMapper;

    @GetMapping
    public ResponseEntity<Page<OwnerPaymentDTO>> getOwnerPayments(@RequestParam(defaultValue = "") String number,
                                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                  @RequestParam(defaultValue = "asc") String order) {
        Page<OwnerPaymentEntity> ownerPaymentEntities = ownerPaymentService.getOwnerPayments(number.trim(), pageNo, pageSize, sortBy, order);
        Page<OwnerPaymentDTO> ownerPaymentDTOS = ownerPaymentEntities.map(ownerPaymentDTOMapper);
        return ResponseEntity.ok(ownerPaymentDTOS);
    }

    @PostMapping
    public ResponseEntity<OwnerPaymentDTO> createOwnerPayment(@RequestBody OwnerPaymentEntity payment) {
        OwnerPaymentEntity ownerPayment = ownerPaymentService.createOwnerPayment(payment);
        OwnerPaymentDTO ownerPaymentDTO = ownerPaymentDTOMapper.apply(ownerPayment);
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerPaymentDTO);
    }

    @PutMapping
    public ResponseEntity<OwnerPaymentDTO> updateOwnerPayment(@RequestBody OwnerPaymentEntity payment) {
        OwnerPaymentEntity ownerPayment = ownerPaymentService.updateOwnerPayment(payment);
        OwnerPaymentDTO ownerPaymentDTO = ownerPaymentDTOMapper.apply(ownerPayment);
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerPaymentDTO);
    }

}
