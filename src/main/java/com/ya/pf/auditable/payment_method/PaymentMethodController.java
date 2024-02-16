package com.ya.pf.auditable.payment_method;

import com.ya.pf.auditable.payment_method.dto.PaymentMethodDTO;
import com.ya.pf.auditable.payment_method.dto.PaymentMethodDTOMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment-method")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    private final PaymentMethodDTOMapper paymentMethodDTOMapper;

    @GetMapping
    public ResponseEntity<Page<PaymentMethodDTO>> getPaymentMethods(@RequestParam(defaultValue = "") String name,
                                                                    @RequestParam(defaultValue = "0") int pageNo,
                                                                    @RequestParam(defaultValue = "10") int pageSize,
                                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                                    @RequestParam(defaultValue = "asc") String order) {

        try {
            Page<PaymentMethodEntity> wayOfPaymentEntities = paymentMethodService.getPaymentMethods(name, pageNo, pageSize, sortBy, order);
            Page<PaymentMethodDTO> wayOfPaymentDTOS = wayOfPaymentEntities.map(paymentMethodDTOMapper);
            return ResponseEntity.ok(wayOfPaymentDTOS);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<PaymentMethodDTO> createPaymentMethod(@RequestBody PaymentMethodEntity wayOfPayment) {

        try {
            PaymentMethodEntity paymentMethodEntity = paymentMethodService.createPaymentMethod(wayOfPayment);
            PaymentMethodDTO paymentMethodDTO = paymentMethodDTOMapper.apply(paymentMethodEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentMethodDTO);
        } catch (EntityExistsException e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethod(@RequestBody PaymentMethodEntity wayOfPayment) {

        try {
            PaymentMethodEntity paymentMethodEntity = paymentMethodService.updatePaymentMethod(wayOfPayment);
            PaymentMethodDTO paymentMethodDTO = paymentMethodDTOMapper.apply(paymentMethodEntity);
            return ResponseEntity.ok(paymentMethodDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable long id) {

        try {
            paymentMethodService.deletePaymentMethod(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PaymentMethodDTO>> searchPaymentMethod(@RequestParam(defaultValue = "") String name) {

        if (name.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            List<PaymentMethodEntity> paymentMethodEntities = paymentMethodService.searchPaymentMethod(name);
            List<PaymentMethodDTO> paymentMethodDTOS = paymentMethodEntities.stream().map(paymentMethodDTOMapper).toList();
            return ResponseEntity.ok(paymentMethodDTOS);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
