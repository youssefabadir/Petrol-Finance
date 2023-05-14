package com.ya.pf.auditable.discount.entity;

import com.ya.pf.auditable.discount.view.DiscountView;
import com.ya.pf.auditable.discount.view.DiscountViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/discount")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscountController {

    private final DiscountService discountService;

    private final DiscountViewService discountViewService;

    @GetMapping
    public ResponseEntity<Page<DiscountView>> getDiscounts(@RequestParam(defaultValue = "") String customerName,
                                                           @RequestParam(defaultValue = "") String productName,
                                                           @RequestParam(defaultValue = "0") int pageNo,
                                                           @RequestParam(defaultValue = "10") int pageSize,
                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                           @RequestParam(defaultValue = "asc") String order) {

        try {
            Page<DiscountView> discountViewPage = discountViewService.getDiscounts(customerName.trim(), productName.trim(), pageNo,
                    pageSize, sortBy, order);
            return ResponseEntity.ok(discountViewPage);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<DiscountEntity> createDiscount(@RequestBody DiscountEntity discount) {

        try {
            DiscountEntity discountEntity = discountService.createDiscount(discount);
            return ResponseEntity.status(HttpStatus.CREATED).body(discountEntity);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<DiscountEntity> updateDiscount(@RequestBody DiscountEntity discount) {

        try {
            DiscountEntity discountEntity = discountService.updateDiscount(discount);
            return ResponseEntity.ok(discountEntity);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable long id) {

        try {
            discountService.deleteDiscount(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
