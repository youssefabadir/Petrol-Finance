package com.ya.pf.auditable.expense;

import com.ya.pf.auditable.payment.PaymentService;
import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentEntity;
import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentService;
import com.ya.pf.auditable.shipment.ShipmentService;
import com.ya.pf.auditable.truck.TruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestValueException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final ShipmentService shipmentService;

    private final OwnerPaymentService ownerPaymentService;

    private final PaymentService paymentService;

    private final TruckService truckService;

    @Override
    public List<ExpenseEntity> getExpenses(long shipmentId) {

        return expenseRepository.findAllByShipment_Id(shipmentId);
    }

    @Override
    public ExpenseEntity createExpense(ExpenseEntity expense, String paymentNumber) throws MissingRequestValueException {

        if (expense.getId() != null) {
            expense.setId(null);
        }
        float amount = expense.getAmount();
        shipmentService.updateShipmentRevenue(expense.getShipment().getId(), Math.abs(amount) * -1);

        OwnerPaymentEntity payment = new OwnerPaymentEntity();
        payment.setPaymentType("OWNER_PAYMENT");
        payment.setAmount(amount);
        payment.setPaymentMethodId(1);
        payment.setTransferred(false);
        payment.setNote(expense.getNote());
        payment.setNumber(paymentNumber);
        OwnerPaymentEntity ownerPayment = ownerPaymentService.createOwnerPayment(payment);
        expense.setOwnerPayment(ownerPayment);

        return expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public ExpenseEntity updateExpense(ExpenseEntity expense, String paymentNumber) throws MissingRequestValueException {

        if (expenseRepository.existsById(expense.getId())) {
            ExpenseEntity oldExpense = expenseRepository.getReferenceById(expense.getId());
            deleteExpense(oldExpense.getId());
            return createExpense(oldExpense, paymentNumber);
        } else {
            throw new EntityNotFoundException("This expense with ID " + expense.getId() + " is not found");
        }
    }

    @Override
    @Transactional
    public void deleteExpense(long id) {

        if (expenseRepository.existsById(id)) {
            ExpenseEntity expense = expenseRepository.getReferenceById(id);
            long shipmentId = expense.getShipment().getId();
            float amount = Math.abs(expense.getAmount());
            long paymentId = expense.getOwnerPayment().getId();

            expenseRepository.deleteById(id);
            shipmentService.updateShipmentRevenue(shipmentId, amount);
            paymentService.deletePaymentById(paymentId);
        } else {
            throw new EntityNotFoundException("This expense with ID " + id + " is not found");
        }
    }

    @Override
    public void deleteExpensesByShipmentId(long shipmentId, long truckId) {

        float totalExpenses = expenseRepository.totalShipmentExpenses(shipmentId);
        truckService.updateTruckBalance(truckId, totalExpenses);
        expenseRepository.deleteAllByShipment_Id(shipmentId);
    }

}
