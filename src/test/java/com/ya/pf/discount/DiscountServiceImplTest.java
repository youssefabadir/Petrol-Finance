package com.ya.pf.discount;

import com.ya.pf.auditable.discount.entity.DiscountEntity;
import com.ya.pf.auditable.discount.entity.DiscountRepository;
import com.ya.pf.auditable.discount.entity.DiscountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiscountServiceImplTest {

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountServiceImpl discountService;

    private DiscountEntity discount;

    @BeforeEach
    public void setUp() {

        discount = new DiscountEntity();
        discount.setDiscountedPrice(2f);
        discount.setCustomerId(1L);
        discount.setProductId(1L);
    }

    @Test
    public void testCreateDiscount() {

        when(discountRepository.save(discount)).thenReturn(discount);

        DiscountEntity savedDiscount = discountService.createDiscount(discount);

        verify(discountRepository).save(discount);
        assertThat(savedDiscount).isEqualTo(discount);
    }

    @Test
    public void testUpdateDiscount() {

        discount.setId(1L);
        discount.setDiscountedPrice(5f);
        discount.setCustomerId(2L);
        discount.setProductId(2L);
        when(discountRepository.existsById(discount.getId())).thenReturn(true);
        when(discountRepository.save(discount)).thenReturn(discount);

        DiscountEntity updatedDiscount = discountService.updateDiscount(discount);

        verify(discountRepository).save(discount);
        assertThat(updatedDiscount).isEqualTo(discount);
    }

    @Test
    public void testUpdateDiscountNotFound() {

        discount.setId(1L);
        when(discountRepository.existsById(discount.getId())).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> discountService.updateDiscount(discount));
    }

    @Test
    public void testDeleteDiscount() {

        long id = 1L;
        when(discountRepository.existsById(id)).thenReturn(true);
        discountService.deleteDiscount(id);
        verify(discountRepository).deleteById(id);
        verify(discountRepository).existsById(id);
    }

    @Test
    public void testDeleteDiscountNotFound() {

        long id = 1L;
        when(discountRepository.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> discountService.deleteDiscount(id));
        verify(discountRepository).existsById(id);
    }

    @Test
    public void testGetCustomerDiscountedPrice() {

        when(discountRepository.findByCustomerIdAndProductId(1L, 1L)).thenReturn(discount);
        float discountedPrice = discountService.getCustomerDiscountedPrice(1L, 1L);
        assertThat(discountedPrice).isEqualTo(2f);
    }

    @Test
    public void testGetCustomerDiscountedPriceNotFound() {

        when(discountRepository.findByCustomerIdAndProductId(1L, 1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> discountService.getCustomerDiscountedPrice(1L, 1L));
    }

}
