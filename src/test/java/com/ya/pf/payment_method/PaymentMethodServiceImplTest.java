package com.ya.pf.payment_method;

import com.ya.pf.auditable.payment_method.PaymentMethodEntity;
import com.ya.pf.auditable.payment_method.PaymentMethodRepository;
import com.ya.pf.auditable.payment_method.PaymentMethodServiceImpl;
import com.ya.pf.util.Helper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodServiceImplTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    private PaymentMethodEntity paymentMethodEntity;

    @BeforeEach
    public void setUp() {

        paymentMethodEntity = new PaymentMethodEntity();
        paymentMethodEntity.setId(1L);
        paymentMethodEntity.setName("Cash");
        paymentMethodEntity.setBalance(0f);
        paymentMethodEntity.setStartBalance(0f);
    }

    @Test
    public void testGetAllPaymentMethods() {

        Pageable pageable = Helper.preparePageable(0, 5, "id", "desc");
        Page<PaymentMethodEntity> page = new PageImpl<>(Collections.singletonList(paymentMethodEntity));
        when(paymentMethodRepository.findAll(pageable)).thenReturn(page);

        Page<PaymentMethodEntity> result = paymentMethodService.getPaymentMethods("", 0, 5, "id", "desc");

        assertThat(result).isEqualTo(page);
        verify(paymentMethodRepository).findAll(pageable);
    }

    @Test
    public void testGetPaymentMethodWithName() {

        Pageable pageable = Helper.preparePageable(0, 5, "id", "desc");
        Page<PaymentMethodEntity> page = new PageImpl<>(Collections.singletonList(paymentMethodEntity));
        when(paymentMethodRepository.findByNameContaining("Cash", pageable)).thenReturn(page);

        Page<PaymentMethodEntity> result = paymentMethodService.getPaymentMethods("Cash", 0, 5, "id", "desc");

        assertThat(result).isEqualTo(page);
        verify(paymentMethodRepository).findByNameContaining("Cash", pageable);
    }

    @Test
    public void testCreatePaymentMethod() {

        when(paymentMethodRepository.save(paymentMethodEntity)).thenReturn(paymentMethodEntity);
        PaymentMethodEntity result = paymentMethodService.createPaymentMethod(paymentMethodEntity);
        assertThat(result).isEqualTo(paymentMethodEntity);
        verify(paymentMethodRepository).save(paymentMethodEntity);
    }

    @Test
    public void testCreatePaymentMethodExists() {

        when(paymentMethodRepository.existsByName("Cash")).thenReturn(true);
        assertThrows(EntityExistsException.class, () -> paymentMethodService.createPaymentMethod(paymentMethodEntity));
        verify(paymentMethodRepository).existsByName("Cash");
    }

    @Test
    public void testUpdatePaymentMethod() {

        long id = 1L;
        paymentMethodEntity.setName("Bank");
        when(paymentMethodRepository.existsById(id)).thenReturn(true);
        when(paymentMethodRepository.getReferenceById(id)).thenReturn(paymentMethodEntity);
        when(paymentMethodRepository.checkUniquePayment(id, paymentMethodEntity.getName())).thenReturn(true);
        when(paymentMethodRepository.save(paymentMethodEntity)).thenReturn(paymentMethodEntity);

        PaymentMethodEntity updatedPayment = paymentMethodService.updatePaymentMethod(paymentMethodEntity);

        verify(paymentMethodRepository).existsById(id);
        verify(paymentMethodRepository).getReferenceById(id);
        verify(paymentMethodRepository).checkUniquePayment(id, paymentMethodEntity.getName());
        verify(paymentMethodRepository).save(paymentMethodEntity);
        assertThat(updatedPayment).isEqualTo(paymentMethodEntity);
    }

    @Test
    public void testUpdatePaymentMethodNotFound() {

        long id = 1L;
        when(paymentMethodRepository.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> paymentMethodService.updatePaymentMethod(paymentMethodEntity));
        verify(paymentMethodRepository).existsById(id);
    }

    @Test
    public void testUpdatePaymentNameExists() {

        long id = 1L;
        when(paymentMethodRepository.existsById(id)).thenReturn(true);
        when(paymentMethodRepository.getReferenceById(id)).thenReturn(paymentMethodEntity);
        when(paymentMethodRepository.checkUniquePayment(id, paymentMethodEntity.getName())).thenReturn(false);
        assertThrows(EntityExistsException.class, () -> paymentMethodService.updatePaymentMethod(paymentMethodEntity));
        verify(paymentMethodRepository).existsById(id);
        verify(paymentMethodRepository).getReferenceById(id);
        verify(paymentMethodRepository).checkUniquePayment(id, paymentMethodEntity.getName());
    }

    @Test
    public void testDeletePaymentMethod() {

        long id = 1L;
        when(paymentMethodRepository.existsById(id)).thenReturn(true);
        paymentMethodService.deletePaymentMethod(id);
        verify(paymentMethodRepository).existsById(id);
        verify(paymentMethodRepository).deleteById(id);
    }

    @Test
    public void testDeletePaymentMethodNotFound() {

        long id = 1L;
        when(paymentMethodRepository.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> paymentMethodService.deletePaymentMethod(id));
        verify(paymentMethodRepository).existsById(id);
    }

    @Test
    public void testSearchPaymentMethod() {

        List<PaymentMethodEntity> list = Collections.singletonList(paymentMethodEntity);
        when(paymentMethodRepository.findByNameContaining("Cash")).thenReturn(list);

        List<PaymentMethodEntity> result = paymentMethodService.searchPaymentMethod("Cash");

        assertThat(result).isEqualTo(list);
        verify(paymentMethodRepository).findByNameContaining("Cash");
    }

    @Test
    public void testGetPaymentMethodById() {

        long id = 1L;
        when(paymentMethodRepository.getReferenceById(id)).thenReturn(paymentMethodEntity);
        PaymentMethodEntity result = paymentMethodService.getPaymentMethodById(id);
        assertThat(result).isEqualTo(paymentMethodEntity);
        verify(paymentMethodRepository).getReferenceById(id);
    }

    @Test
    public void testUpdatePaymentMethodBalance() {

        paymentMethodService.updatePaymentMethodBalance(1L, 100f);
        verify(paymentMethodRepository).updatePaymentMethodBalance(1L, 100f);
    }

}
