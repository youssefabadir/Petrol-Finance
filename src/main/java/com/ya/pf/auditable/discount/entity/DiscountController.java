package com.ya.pf.auditable.discount.entity;

import com.ya.pf.auditable.discount.view.DiscountView;
import com.ya.pf.auditable.discount.view.DiscountViewService;
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
        Page<DiscountView> discountViewPage = discountViewService.getDiscounts(customerName.trim(), productName.trim(), pageNo, pageSize, sortBy,
                                                                               order);
        return ResponseEntity.ok(discountViewPage);
    }

    @PostMapping
    public ResponseEntity<DiscountEntity> createDiscount(@RequestBody DiscountEntity discount) {
        DiscountEntity discountEntity = discountService.createDiscount(discount);
        return ResponseEntity.status(HttpStatus.CREATED).body(discountEntity);
    }

    @PutMapping
    public ResponseEntity<DiscountEntity> updateDiscount(@RequestBody DiscountEntity discount) {
        DiscountEntity discountEntity = discountService.updateDiscount(discount);
        return ResponseEntity.ok(discountEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }

}
