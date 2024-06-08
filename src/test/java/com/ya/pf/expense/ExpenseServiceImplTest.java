package com.ya.pf.expense;

import com.ya.pf.auditable.expense.ExpenseEntity;
import com.ya.pf.auditable.expense.ExpenseRepository;
import com.ya.pf.auditable.expense.ExpenseServiceImpl;
import com.ya.pf.auditable.payment.PaymentService;
import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentEntity;
import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentService;
import com.ya.pf.auditable.shipment.ShipmentEntity;
import com.ya.pf.auditable.shipment.ShipmentService;
import com.ya.pf.auditable.truck.TruckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MissingRequestValueException;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ShipmentService shipmentService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private OwnerPaymentService ownerPaymentService;

    @Mock
    private TruckService truckService;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private ExpenseEntity expenseEntity;

    @BeforeEach
    public void setUp() {

        ShipmentEntity shipmentEntity = new ShipmentEntity();
        shipmentEntity.setId(1L);

        OwnerPaymentEntity ownerPayment = new OwnerPaymentEntity();
        ownerPayment.setId(1L);

        expenseEntity = new ExpenseEntity();
        expenseEntity.setAmount(100f);
        expenseEntity.setShipment(shipmentEntity);
        expenseEntity.setOwnerPayment(ownerPayment);
    }

    @Test
    public void testCreateExpense() throws MissingRequestValueException {

        when(expenseRepository.save(expenseEntity)).thenReturn(expenseEntity);

        ExpenseEntity createdExpense = expenseService.createExpense(expenseEntity, 1L);

        verify(expenseRepository).save(expenseEntity);
        verify(ownerPaymentService).createOwnerPayment(any(OwnerPaymentEntity.class));
        assertThat(expenseEntity).isEqualTo(createdExpense);
    }

    @Test
    public void testUpdateExpense() throws MissingRequestValueException {

        long id = 1L;
        expenseEntity.setId(id);
        expenseEntity.setAmount(200f);
        when(expenseRepository.existsById(id)).thenReturn(true);
        when(expenseRepository.getReferenceById(id)).thenReturn(expenseEntity);
        when(expenseRepository.save(expenseEntity)).thenReturn(expenseEntity);

        ExpenseEntity updatedExpense = expenseService.updateExpense(expenseEntity, 2L);

        assertThat(updatedExpense).isEqualTo(expenseEntity);
        verify(expenseRepository, times(2)).existsById(id);
        verify(expenseRepository, times(2)).getReferenceById(id);
        verify(expenseRepository).deleteById(id);
        verify(expenseRepository).save(expenseEntity);
    }

    @Test
    public void testUpdateExpenseNotFound() {

        long id = 1L;
        expenseEntity.setId(id);
        when(expenseRepository.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> expenseService.updateExpense(expenseEntity, 1L));
        verify(expenseRepository).existsById(id);
    }

    @Test
    public void testDeleteExpense() {

        long id = 1L;
        when(expenseRepository.existsById(id)).thenReturn(true);
        when(expenseRepository.getReferenceById(id)).thenReturn(expenseEntity);

        expenseService.deleteExpense(id);

        verify(expenseRepository).existsById(id);
        verify(expenseRepository).getReferenceById(id);
        verify(expenseRepository).deleteById(id);
        verify(shipmentService).updateShipmentRevenue(id, expenseEntity.getAmount());
        verify(paymentService).deletePaymentById(expenseEntity.getOwnerPayment().getId());
    }

    @Test
    public void testDeleteExpenseNotFound() {

        long id = 1L;
        when(expenseRepository.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> expenseService.deleteExpense(id));
        verify(expenseRepository).existsById(id);
    }

    @Test
    public void testDeleteExpenseByShipmentId() {

        long shipmentId = 1L;
        long truckId = 1L;
        when(expenseRepository.totalShipmentExpenses(shipmentId)).thenReturn(200f);

        expenseService.deleteExpensesByShipmentId(shipmentId, truckId);

        verify(truckService).updateTruckBalance(truckId, -200f);
        verify(expenseRepository).deleteAllByShipment_Id(shipmentId);
    }

}
