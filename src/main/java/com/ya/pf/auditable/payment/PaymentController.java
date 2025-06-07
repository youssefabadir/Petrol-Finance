package com.ya.pf.auditable.payment;

import com.ya.pf.auditable.payment.dto.PaymentDTO;
import com.ya.pf.auditable.payment.dto.PaymentDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentController {

    private final PaymentService paymentService;

    private final PaymentDTOMapper paymentDTOMapper;

    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> getPayments(@RequestParam(defaultValue = "-1") long paymentMethodId,
                                                        @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String order,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate start,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate end) {
        Page<PaymentEntity> paymentEntities = paymentService.getPayments(paymentMethodId, pageNo, pageSize, sortBy, order, start, end);
        Page<PaymentDTO> paymentDTOS = paymentEntities.map(paymentDTOMapper);
        return ResponseEntity.ok(paymentDTOS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable long id) {
        paymentService.deletePaymentById(id);
        return ResponseEntity.noContent().build();
    }

}
