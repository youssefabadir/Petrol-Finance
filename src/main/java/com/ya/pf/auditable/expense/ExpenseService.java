package com.ya.pf.auditable.expense;

import org.springframework.web.bind.MissingRequestValueException;

import java.util.List;

public interface ExpenseService {

    List<ExpenseEntity> getExpenses(long shipmentId);

    ExpenseEntity createExpense(ExpenseEntity expense, String paymentNumber) throws MissingRequestValueException;

    ExpenseEntity updateExpense(ExpenseEntity expense, String paymentNumber) throws MissingRequestValueException;

    void deleteExpense(long id);

    void deleteExpensesByShipmentId(long shipmentId, long truckId);

}
