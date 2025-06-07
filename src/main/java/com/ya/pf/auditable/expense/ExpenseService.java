package com.ya.pf.auditable.expense;

import java.util.List;

public interface ExpenseService {

    List<ExpenseEntity> getExpenses(long shipmentId);

    ExpenseEntity createExpense(ExpenseEntity expense, long paymentMethodId);

    ExpenseEntity updateExpense(ExpenseEntity expense, long paymentMethodId);

    void deleteExpense(long id);

    void deleteExpensesByShipmentId(long shipmentId, long truckId);

}
