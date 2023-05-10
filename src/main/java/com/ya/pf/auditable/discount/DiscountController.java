package com.ya.pf.auditable.discount;

import com.ya.pf.auditable.discount.dto.DiscountDTO;
import com.ya.pf.auditable.discount.dto.DiscountDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/discount")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscountController {

    private final DiscountService discountService;

    private final DiscountDTOMapper discountDTOMapper;

    @PostMapping
    public ResponseEntity<DiscountDTO> createDiscount(@RequestBody DiscountEntity customerDiscount) {

        DiscountEntity discountEntity = discountService.createDiscount(customerDiscount);
        DiscountDTO discountDTO = discountDTOMapper.apply(discountEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(discountDTO);
    }

    @PutMapping
    public ResponseEntity<DiscountDTO> updateDiscount(@RequestBody DiscountEntity customerDiscount) {

        try {
            DiscountEntity discountEntity = discountService.updateDiscount(customerDiscount);
            DiscountDTO discountDTO = discountDTOMapper.apply(discountEntity);
            return ResponseEntity.ok(discountDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
