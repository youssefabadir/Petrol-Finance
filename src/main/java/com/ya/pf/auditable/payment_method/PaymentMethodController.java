package com.ya.pf.auditable.payment_method;

import com.ya.pf.auditable.payment_method.dto.PaymentMethodDTO;
import com.ya.pf.auditable.payment_method.dto.PaymentMethodDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
        Page<PaymentMethodEntity> wayOfPaymentEntities = paymentMethodService.getPaymentMethods(name, pageNo, pageSize, sortBy, order);
        Page<PaymentMethodDTO> wayOfPaymentDTOS = wayOfPaymentEntities.map(paymentMethodDTOMapper);
        return ResponseEntity.ok(wayOfPaymentDTOS);
    }

    @PostMapping
    public ResponseEntity<PaymentMethodDTO> createPaymentMethod(@RequestBody PaymentMethodEntity wayOfPayment) {
        PaymentMethodEntity paymentMethodEntity = paymentMethodService.createPaymentMethod(wayOfPayment);
        PaymentMethodDTO paymentMethodDTO = paymentMethodDTOMapper.apply(paymentMethodEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentMethodDTO);
    }

    @PutMapping
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethod(@RequestBody PaymentMethodEntity wayOfPayment) {
        PaymentMethodEntity paymentMethodEntity = paymentMethodService.updatePaymentMethod(wayOfPayment);
        PaymentMethodDTO paymentMethodDTO = paymentMethodDTOMapper.apply(paymentMethodEntity);
        return ResponseEntity.ok(paymentMethodDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable long id) {
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PaymentMethodDTO>> searchPaymentMethod(@RequestParam(defaultValue = "") String name) {
        if (name.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<PaymentMethodEntity> paymentMethodEntities = paymentMethodService.searchPaymentMethod(name);
        List<PaymentMethodDTO> paymentMethodDTOS = paymentMethodEntities.stream().map(paymentMethodDTOMapper).toList();
        return ResponseEntity.ok(paymentMethodDTOS);
    }

}
